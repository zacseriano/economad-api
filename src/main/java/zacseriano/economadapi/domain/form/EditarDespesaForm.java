package zacseriano.economadapi.domain.form;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditarDespesaForm {
	@NotNull
	private UUID id;
	@NotNull
	private BigDecimal valor;
	@NotNull
	private StatusDespesaEnum statusDespesaEnum;
}
