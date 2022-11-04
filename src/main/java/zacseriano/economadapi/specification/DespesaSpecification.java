package zacseriano.economadapi.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.domain.model.Despesa;

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
}
