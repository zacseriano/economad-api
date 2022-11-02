package zacseriano.economadapi.domain.enums;

import lombok.Getter;

@Getter
public enum StatusDespesaEnum {
	NÃO_PAGO,
	PAGO,
	PAGO_PARCIALMENTE,
	ATRASADO;
}
