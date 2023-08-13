package zacseriano.economadapi.domain.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
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
