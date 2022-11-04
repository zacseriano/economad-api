package zacseriano.economadapi.specification;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import zacseriano.economadapi.domain.model.Despesa;

public class DespesaSpecificationBuilder {
	public static Specification<Despesa> builder(String descricaoCompetencia,
			String nomePagador, String tipoPagamentoPagador, String nomeOrigem) {

		var specification = DespesaSpecification.naoDeletados();
		Map<Specification<Despesa>, String> specsMap = new HashMap<Specification<Despesa>, String>();

		filtroDescricaoCompetencia(descricaoCompetencia, specsMap);

		var specifications = specsMap.keySet();
		for (var spec : specifications) {
			var conditional = specsMap.get(spec);
			if (conditional.equals("and")) {
				specification = specification.and(spec);
			} else {
				specification = specification.or(spec);
			}
		}
		return specification;
	}

	private static void filtroDescricaoCompetencia(String descricaoCompetencia,
			Map<Specification<Despesa>, String> specs) {
		if (descricaoCompetencia != null) {
			var spec = DespesaSpecification.descricaoCompetencia(descricaoCompetencia);
			if (spec != null) {
				specs.put(spec, "and");
			}
		}
	}

}
