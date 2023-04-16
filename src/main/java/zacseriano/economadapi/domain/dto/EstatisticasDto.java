package zacseriano.economadapi.domain.dto;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticasDto {
	private String nomePagador;
	private BigDecimal total;
	
	@JsonIgnore
	public static void ordenarEstatisticasPorValorDecrescente(List<EstatisticasDto> estatisticas) {
        Comparator<EstatisticasDto> comparator = new Comparator<EstatisticasDto>() {
            @Override
            public int compare(EstatisticasDto e1, EstatisticasDto e2) {
                return e2.getTotal().compareTo(e1.getTotal());
            }
        };
        Collections.sort(estatisticas, comparator);
    }
}
