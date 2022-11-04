package zacseriano.economadapi.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;

@Getter
@Setter
@NoArgsConstructor
public class DespesaDto {
	private UUID id;
	private String descricao;
	private BigDecimal valor;
	private LocalDate data;	
	private LocalDate prazo;
	private StatusDespesaEnum statusDespesaEnum;
	private String origem;
	private String pagador;
	private String competencia;	
}
