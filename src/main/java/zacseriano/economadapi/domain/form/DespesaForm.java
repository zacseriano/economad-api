package zacseriano.economadapi.domain.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.shared.validator.data.StringAsLocalDateValid;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaForm {
	@NotBlank
	private String descricao;
	
	private BigDecimal valor;
	@StringAsLocalDateValid
	private String data;	
	private String prazo;
	private String parcela;
	private StatusDespesaEnum statusDespesaEnum;
	@NotNull
	private CompetenciaForm competenciaForm;
	@NotNull
	private OrigemForm origemForm;
	@NotNull
	private PagadorForm pagadorForm;
}
