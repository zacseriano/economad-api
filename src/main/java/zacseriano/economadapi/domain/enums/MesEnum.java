package zacseriano.economadapi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MesEnum {
	JANEIRO("01"),
	FEVEREIRO("02"),
	MARÇO("03"),
	ABRIL("04"),
	MAIO("05"),
	JUNHO("06"),
	JULHO("07"),
	AGOSTO("08"),
	SETEMBRO("09"),
	OUTUBRO("10"),
	NOVEMBRO("11"),
	DEZEMBRO("12");
	
	private String numeroMes;
	
}
