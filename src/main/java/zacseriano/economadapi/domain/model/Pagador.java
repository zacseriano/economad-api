package zacseriano.economadapi.domain.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

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
	@Column(unique = true)
	private String nome;
	@Enumerated(EnumType.STRING)
	private TipoPagamentoEnum tipoPagamentoEnum;
	@OneToMany(mappedBy = "pagador", cascade = CascadeType.ALL)
	private List<Despesa> despesas;
}
