package zacseriano.economadapi.domain.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zacseriano.economadapi.domain.enums.TipoPagamentoEnum;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pagador extends BaseModel {
	private String nome;
	@Enumerated(EnumType.STRING)
	private TipoPagamentoEnum tipoPagamentoEnum;
}
