package zacseriano.economadapi.service.origem;

import javax.validation.ValidationException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import zacseriano.economadapi.domain.form.OrigemForm;

@Component
@RequiredArgsConstructor
public class OrigemValidator {
	public void validarForm (OrigemForm form) {
		if (form.getNome().isBlank()) {
			throw new ValidationException("Informe o nome da Origem");
		}
		
//		if (form.getCidade().isBlank()) {
//			throw new ValidationException("Informe a cidade da Origem");
//		}
//		
//		if (form.getEstado().isBlank()) {
//			throw new ValidationException("Informe o estado da Origem");
//		}
	}
}
