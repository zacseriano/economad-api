package zacseriano.economadapi.domain.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zacseriano.economadapi.shared.validator.data.StringAsLocalDateValid;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaSimplificadaForm {
	@NotBlank
	private String descricao;
	private BigDecimal valor;
	@StringAsLocalDateValid
	@NotNull
	private String data;
	@StringAsLocalDateValid
	private String prazo;
	private Integer numeroParcelas;
	private String nomeOrigem;
	private String nomePagador;
}
