package zacseriano.economadapi.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import zacseriano.economadapi.domain.enums.TipoPagamentoEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagadorForm {
	private String nome;
	private TipoPagamentoEnum tipoPagamentoEnum;
}
