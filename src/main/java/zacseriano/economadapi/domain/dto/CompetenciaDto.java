package zacseriano.economadapi.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompetenciaDto {
	private UUID id;
	private LocalDate data;
	private String descricao;
	private BigDecimal salario;
}
