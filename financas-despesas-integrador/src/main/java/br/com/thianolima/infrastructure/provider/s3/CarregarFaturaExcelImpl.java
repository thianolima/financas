package br.com.thianolima.infrastructure.provider.s3;

import br.com.thianolima.core.provider.BuscarCartaoPorNumeroFinal;
import br.com.thianolima.core.provider.CarregarFaturaExcel;
import br.com.thianolima.model.Cartao;
import br.com.thianolima.model.Despesa;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class CarregarFaturaExcelImpl implements CarregarFaturaExcel {

    private final S3Client s3Client;
    private final DataFormatter dataFormatter;
    private final BuscarCartaoPorNumeroFinal buscarCartaoPorNumeroFinal;
    private static final Pattern PATTERN_PARCELA = Pattern.compile(".*?(\\d+)\\s+de\\s+(\\d+)", Pattern.CASE_INSENSITIVE);

    public CarregarFaturaExcelImpl(S3Client s3Client, BuscarCartaoPorNumeroFinal buscarCartaoPorNumeroFinal) {
        this.s3Client = s3Client;
        this.buscarCartaoPorNumeroFinal = buscarCartaoPorNumeroFinal;
        this.dataFormatter = new DataFormatter();
    }

    @Override
    public List<Despesa> executar(
            Long usuarioId,
            String s3Bucket,
            String s3Key
    ) {
        try {
            List<Despesa> despesas = new ArrayList<Despesa>();
            Map<String, Optional<Long>> cacheCartoes = new HashMap<>();

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Bucket)
                    .key(s3Key)
                    .build();

            InputStream inputStream = s3Client.getObject(getObjectRequest);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            LocalDate dataVencimentoFatura = obterData(sheet.getRow(9).getCell(8));

            for (var linhaIndex = 15; linhaIndex <= sheet.getLastRowNum(); linhaIndex++) {
                Row row = sheet.getRow(linhaIndex);
                if (isFimPlanilha(row)) {
                    break;
                }
                try {
                    LocalDate dataVencimento = dataVencimentoFatura;
                    LocalDate dataDespesa = obterData(row.getCell(1));
                    String descricaoOriginal = obterTexto(row.getCell(2));
                    BigDecimal valor = obterBigDecimal(row.getCell(4));
                    String parcelaDescricao = obterTexto(row.getCell(3));
//                    String titularidade = obterTexto(row.getCell(6));
//                    String responsavelCartao = obterTexto(row.getCell(7));
//                    String tipoCartao = obterTexto(row.getCell(8));
                    String numeroFinalCartao = obterTexto(row.getCell(9)).replace("*","").trim();

                    cacheCartoes.computeIfAbsent(numeroFinalCartao, numero ->
                            buscarCartaoPorNumeroFinal.executar(numero, usuarioId)
                                    .map(Cartao::getId)
                    ).ifPresent( cartaoId ->
                            despesas.add(
                                    Despesa.builder()
                                            .usuarioId(usuarioId)
                                            .cartaoId(cartaoId)
                                            .dataDespesa(dataDespesa)
                                            .dataVencimento(dataVencimento)
                                            .descricaoOriginal(descricaoOriginal)
                                            .valor(valor)
                                            .parcelaAtual(extrairParcelaAtual(parcelaDescricao))
                                            .totalParcelas(extrairTotalParcelas(parcelaDescricao))
                                            .build()
                            )
                    );
                } catch (Exception e) {
                    log.error("Erro ao processar dados da linha {}: {}", linhaIndex + 1, e.getMessage());
                }
            }

            return despesas;
        } catch (Exception e) {
            log.error("Erro na leitura do arquivo Excel vindo do S3: {}", s3Key, e);
            throw new RuntimeException(e);
        }
    }

    private boolean isFimPlanilha(Row row) {
        if (row == null) {
            return true;
        }
        Cell cell = row.getCell(1);
        return cell == null || cell.getCellType() == CellType.BLANK || dataFormatter.formatCellValue(cell).trim().isEmpty();
    }

    private String obterTexto(Cell cell) {
        if (cell == null) {
            return "";
        }
        return dataFormatter.formatCellValue(cell).trim();
    }

    private LocalDate obterData(Cell cell) {
        if (cell == null) {
            return null;
        }
        // Se a célula do Excel estiver configurada nativamente como data
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        // Fallback caso a data esteja digitada como texto simples na planilha (ex: "20/06/2026")
        String stringValue = dataFormatter.formatCellValue(cell).trim();
        if (stringValue.isEmpty()) {
            return null;
        }
        return LocalDate.parse(stringValue, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private BigDecimal obterBigDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        // Se a célula estiver formatada como Número/Moeda nativo no Excel
        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }

        // Fallback caso o valor esteja digitado como texto
        String texto = dataFormatter.formatCellValue(cell).trim();
        if (texto.isEmpty()) {
            return BigDecimal.ZERO;
        }
        // Limpa formatações pontuais de texto para conversão segura
        String valorLimpo = texto.replace("R$", "").replace(".", "").replace(",", ".").trim();
        return new BigDecimal(valorLimpo);
    }

    private Integer extrairParcelaAtual(String parcelaDescricao) {
        if (parcelaDescricao == null || parcelaDescricao.isEmpty()) {
            return null;
        }

        Matcher matcher = PATTERN_PARCELA.matcher(parcelaDescricao);
        if (matcher.find()) {
            // Grupo 1 captura o primeiro conjunto de dígitos (ex: 19)
            return Integer.parseInt(matcher.group(1));
        }

        return null;
    }

    private Integer extrairTotalParcelas(String parcelaDescricao) {
        if (parcelaDescricao == null || parcelaDescricao.isEmpty()) {
            return null;
        }

        Matcher matcher = PATTERN_PARCELA.matcher(parcelaDescricao);
        if (matcher.find()) {
            // Grupo 2 captura o segundo conjunto de dígitos (ex: 21)
            return Integer.parseInt(matcher.group(2));
        }

        return null;
    }
}