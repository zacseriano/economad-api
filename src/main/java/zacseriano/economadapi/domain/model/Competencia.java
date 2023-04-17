package zacseriano.economadapi.domain.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zacseriano.economadapi.domain.enums.MesEnum;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Competencia extends BaseModel {
	@Enumerated(EnumType.STRING)
	private MesEnum mesEnum;
	private String descricao;
	private int ano;
	private BigDecimal salario;
}
