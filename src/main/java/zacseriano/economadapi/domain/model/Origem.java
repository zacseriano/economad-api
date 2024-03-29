package zacseriano.economadapi.domain.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Origem extends BaseModel {
	@Column(unique = true)
	private String nome;
	private String cidade;
	private String estado;
	@OneToMany(mappedBy = "origem", cascade = CascadeType.ALL)
	private List<Despesa> despesas;
}
