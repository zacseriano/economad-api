package zacseriano.economadapi.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Despesa extends BaseModel {
	private String descricao;
	private LocalDate data;
	private BigDecimal valor;
	@ManyToOne(cascade = CascadeType.ALL)
	private Origem origem;
	@ManyToOne(cascade = CascadeType.ALL)
	private Competencia competencia;
	@ManyToOne(cascade = CascadeType.ALL)
	private Pagador pagador;
	
}
