package zacseriano.economadapi.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Origem;
import zacseriano.economadapi.domain.model.Pagador;

public class DespesaSpecification extends GenericSpecification<Despesa> {
	public static Specification<Despesa> naoDeletados() {
        return createSpecification(criarFiltro(QueryOperator.EQUAL, "deleted", Boolean.FALSE));
    }
	public static Specification<Despesa> descricaoCompetencia(String descricaoCompetencia) {
    	return (Root<Despesa> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Join<Despesa, Competencia> reembolsoJoin = root.join("competencia", JoinType.LEFT);
            query.distinct(true);
            return builder.equal(reembolsoJoin.get("descricao"), descricaoCompetencia);
        };
    }
	public static Specification<Despesa> nomePagador(String nomePagador) {
    	return (Root<Despesa> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Join<Despesa, Pagador> reembolsoJoin = root.join("pagador", JoinType.LEFT);
            query.distinct(true);
            return builder.equal(reembolsoJoin.get("nome"), nomePagador);
        };
    }
	public static Specification<Despesa> nomeOrigem(String nomeOrigem) {
		return (Root<Despesa> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            Join<Despesa, Origem> reembolsoJoin = root.join("origem", JoinType.LEFT);
            query.distinct(true);
            return builder.equal(reembolsoJoin.get("nome"), nomeOrigem);
        };
	}
	public static Specification<Despesa> statusDespesa(StatusDespesaEnum statusDespesaEnum) {
        return createSpecification(criarFiltro(QueryOperator.EQUAL, "statusDespesaEnum", statusDespesaEnum));
    }

}
