package zacseriano.economadapi.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zacseriano.economadapi.domain.enums.MesEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetenciaForm {
	private MesEnum mesEnum;
	private int ano;
}
