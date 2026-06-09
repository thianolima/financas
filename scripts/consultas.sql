select c.nome as categoria, sum(d.valor) as valor
from tb_despesas d
         inner join tb_categorias c on c.categoria_id = d.categoria_id
         inner join tb_faturas f on f.fatura_id = d.fatura_id and f.ano_mes = "202605"
group by c.nome;


select
    d.fatura_id as fatura_id,
    d.despesa_id as despesa_id,
    d.data_despesa  as data,
    t.bandeira as bandeira,
    d.descricao_processada as descricao,
    d.observacao as observacao,
    d.parcela_atual,
    d.total_parcelas,
    c.nome as categoria,
    d.valor as valor_parcela,
    d.recorrente
from tb_despesas d
         inner join tb_faturas f on f.fatura_id  = d.fatura_id
         inner join tb_cartoes t on t.cartao_id = d.cartao_id
         left join tb_categorias c on c.categoria_id = d.categoria_id
where f.ano_mes = "202605"
-- and d.categoria_id is null
  and (d.parcela_atual < d.total_parcelas or d.recorrente =  true)
    #and c.nome is null and d.observacao is null
#and d.observacao  is not null and d.parcela_atual = 1
order by d.cartao_id, d.recorrente, d.data_despesa asc;


select sum(d.valor)
from tb_despesas d
         inner join tb_faturas f on f.fatura_id  = d.fatura_id
         inner join tb_cartoes t on t.cartao_id = d.cartao_id
         left join tb_categorias c on c.categoria_id = d.categoria_id
where f.ano_mes = "202605"
  and (d.parcela_atual < d.total_parcelas or d.recorrente =  true);


delete from tb_despesas d where d.fatura_id IN (select fatura_id from tb_faturas where ano_mes = "202606");
delete from tb_faturas d where d.ano_mes = "202606";


UPDATE tb_despesas
SET recorrente = true
WHERE despesa_id IN (
    SELECT id FROM (
                       SELECT despesa_id AS id
                       FROM tb_despesas
                       WHERE descricao_processada LIKE '%MARIA CLAUDIA%'
                   ) AS tmp
);


select distinct d.descricao_original
from tb_despesas d
         inner join tb_faturas f on f.fatura_id  = d.fatura_id
         inner join tb_cartoes t on t.cartao_id = d.cartao_id
         left join tb_categorias c on c.categoria_id = d.categoria_id
where f.ano_mes in ("202604")
  and d.observacao is null and d.categoria_id is null;


update tb_despesas td
set categoria_id = 20
where td.fatura_id = 143
  and td.data_despesa BETWEEN '2026-04-16' and '2026-04-24'


SELECT
    MAX(d.despesa_id) as despesa_id,
    MAX(d.fatura_id) as fatura_id, -- MAX para não quebrar o agrupamento entre meses
    d.cartao_id,
    d.usuario_id,
    MAX(d.categoria_id) as categoria_id,
    MAX(d.fornecedor_id) as fornecedor_id,
    MAX(d.descricao_original) as descricao_original, -- MAX caso mude o "1/12" na string
    d.descricao_processada,
    d.observacao,
    MAX(d.parcela_atual) as parcela_atual,
    d.total_parcelas,
    MAX(d.sequencia) as sequencia,
    d.data_despesa,
    d.valor,
    d.recorrente,
    MAX(d.data_vencimento) as data_vencimento
FROM tb_despesas d
WHERE d.usuario_id = 1
  AND d.cartao_id IS NOT NULL
  AND d.fatura_id IS NOT NULL
  AND d.recorrente IS TRUE
  AND EXTRACT(YEAR_MONTH FROM d.data_vencimento) >= EXTRACT(YEAR_MONTH FROM CURRENT_DATE())
GROUP BY
    d.cartao_id,
    d.usuario_id,
    d.descricao_processada,
    d.observacao,
    d.total_parcelas,
    d.data_despesa,
    d.valor,
    d.recorrente;