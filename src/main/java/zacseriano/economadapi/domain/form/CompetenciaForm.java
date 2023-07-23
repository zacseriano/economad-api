package zacseriano.economadapi.domain.form;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetenciaForm {
	private LocalDate data;
	private BigDecimal salario;
}
