package zacseriano.economadapi.specification.builder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.specification.filter.DespesaFilter;
import zacseriano.economadapi.specification.spec.DespesaSpecification;

public class DespesaSpecificationBuilder {
	public static Specification<Despesa> builder(DespesaFilter filtro) {
		var specification = DespesaSpecification.naoDeletados();
		Map<Specification<Despesa>, String> specsMap = new HashMap<Specification<Despesa>, String>();

		filtroDescricaoCompetencia(filtro.getDescricaoCompetencia(), specsMap);
		filtroNomePagador(filtro.getNomePagador(), specsMap);
		filtroNomeOrigem(filtro.getNomeOrigem(), specsMap);
		filtroStatusDespesa(filtro.getStatusDespesaEnum(), specsMap);
		filtroDataInicio(filtro.getDataInicio(), specsMap);
		filtroDataFim(filtro.getDataFim(), specsMap);
		
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

	private static void filtroDataInicio(LocalDate dataInicio, Map<Specification<Despesa>, String> specs) {
		if(dataInicio != null) {
			var spec = DespesaSpecification.dataInicio(dataInicio);
			if (spec != null) {
				specs.put(spec, "and");
			}
		}		
	}

	private static void filtroDataFim(LocalDate dataFim, Map<Specification<Despesa>, String> specs) {
		if(dataFim != null) {
			var spec = DespesaSpecification.dataFim(dataFim);
			if (spec != null) {
				specs.put(spec, "and");
			}
		}
	}

	private static void filtroNomeOrigem(String nomeOrigem, Map<Specification<Despesa>, String> specs) {
		if (nomeOrigem != null) {
			var spec = DespesaSpecification.nomeOrigem(nomeOrigem);
			if (spec != null) {
				specs.put(spec, "and");
			}
		}
		
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
	
	private static void filtroNomePagador(String nomePagador,
			Map<Specification<Despesa>, String> specs) {
		if (nomePagador != null) {
			var spec = DespesaSpecification.nomePagador(nomePagador);
			if (spec != null) {
				specs.put(spec, "and");
			}
		}
	}
	
	private static void filtroStatusDespesa(StatusDespesaEnum statusDespesaEnum,
			Map<Specification<Despesa>, String> specs) {
		if (statusDespesaEnum != null) {
			var spec = DespesaSpecification.statusDespesa(statusDespesaEnum);
			if (spec != null) {
				specs.put(spec, "and");
			}
		}
	}

}
