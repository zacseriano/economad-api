package zacseriano.economadapi.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Despesa extends BaseModel {
	private String descricao;
	private BigDecimal valor;
	private LocalDate data;	
	private LocalDate prazo;
	private String parcela;
	@Enumerated(EnumType.STRING)
	private StatusDespesaEnum status;
	@ManyToOne(cascade = CascadeType.ALL)
	private Origem origem;
	@ManyToOne(cascade = CascadeType.ALL)
	private Competencia competencia;
	@ManyToOne(cascade = CascadeType.ALL)
	private Pagador pagador;
	
}
