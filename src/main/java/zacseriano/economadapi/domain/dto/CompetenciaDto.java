package zacseriano.economadapi.domain.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;
import zacseriano.economadapi.domain.enums.MesEnum;

@Data
@NoArgsConstructor
public class CompetenciaDto {
	private Long id;
	private UUID uuid;
	private MesEnum mesEnum;
	private String descricao;
	private int ano;
	private BigDecimal salario;
}
