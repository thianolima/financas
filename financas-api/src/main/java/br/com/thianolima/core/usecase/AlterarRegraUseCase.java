package br.com.thianolima.core.usecase;

import br.com.thianolima.core.provider.database.BuscarRegraPorId;
import br.com.thianolima.core.provider.database.BuscarRegraPorTermoBusca;
import br.com.thianolima.core.provider.database.SalvarRegra;
import br.com.thianolima.model.Regra;
import br.com.thianolima.model.RegraTermo;

import java.util.List;

public class AlterarRegraUseCase {

    private final SalvarRegra salvarRegra;
    private final BuscarRegraPorTermoBusca buscarRegraPorTermoBusca;
    private final BuscarRegraPorId buscarRegraPorId;

    public AlterarRegraUseCase(
            SalvarRegra salvarRegra,
            BuscarRegraPorTermoBusca buscarRegraPorTermoBusca,
            BuscarRegraPorId buscarRegraPorId
    ) {
        this.salvarRegra = salvarRegra;
        this.buscarRegraPorTermoBusca = buscarRegraPorTermoBusca;
        this.buscarRegraPorId = buscarRegraPorId;
    }

    public void executar(Regra regra){
        salvarRegra.executar(
            validarRegra(regra)
        );
    }

    private Regra validarRegra(Regra regra){
        var regraSalva = existeRegra(regra.getId(), regra.getUsuarioId());
        validarTermos(regraSalva);
        regraSalva.setCategoriaId(regra.getCategoriaId());
        regraSalva.setDescricao(regra.getDescricao());
        var novaListatermos = criarNovaListaTermos(regra.getTermos(), regraSalva.getTermos());
        regraSalva.setTermos(novaListatermos);
        return regraSalva;
    }

    private List<RegraTermo> criarNovaListaTermos(
            List<RegraTermo> termosNovos,
            List<RegraTermo> termosSalvo
    ) {
        return termosNovos.stream().map(termoNovo -> {
                termosSalvo.stream()
                        .filter(termo -> termo.getTermoBusca().equals(termoNovo.getTermoBusca()))
                        .findFirst()
                        .ifPresent(termo -> {
                            termoNovo.setId(termo.getId());
                            termoNovo.setRegraId(termo.getRegraId());
                        });
                return termoNovo;
        }).toList();
    }

    private Regra existeRegra(Long regraId, Long usuarioId){
        return buscarRegraPorId.executar(regraId, usuarioId)
                .orElseThrow(() -> new RuntimeException("Regra não encontrada: " + regraId));
    }

    private void validarTermos(Regra regra){
        regra.getTermos().forEach(regraTermo -> {
            var regraSalva = buscarRegraPorTermoBusca.executar(regraTermo.getTermoBusca(), regra.getUsuarioId());
            if(regraSalva.isPresent() && !regraSalva.get().getId().equals(regra.getId()))
                throw new RuntimeException("Termo já utilizado na regra:" + regra.getId());
        });
    }

}
