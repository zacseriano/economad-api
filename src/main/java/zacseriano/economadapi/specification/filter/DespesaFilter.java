package zacseriano.economadapi.specification.filter;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DespesaFilter {
	private String descricaoCompetencia;
	private String nomePagador;
	private String tipoPagamentoPagador; 
	private String nomeOrigem;
	private StatusDespesaEnum statusDespesaEnum;
	private LocalDate dataInicio;
	private LocalDate dataFim;
}
