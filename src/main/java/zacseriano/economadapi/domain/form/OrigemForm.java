package zacseriano.economadapi.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrigemForm {
	private String nome;
	private String cidade;
	private String estado;
}
