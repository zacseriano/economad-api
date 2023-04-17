package zacseriano.economadapi.domain.form;

import java.math.BigDecimal;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zacseriano.economadapi.domain.enums.MesEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetenciaForm {
	@Enumerated(EnumType.STRING)
	private MesEnum mesEnum;
	private int ano;
	private BigDecimal salario;
}
