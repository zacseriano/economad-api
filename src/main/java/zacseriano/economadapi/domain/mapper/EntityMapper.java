package zacseriano.economadapi.domain.mapper;

import java.util.List;

public interface EntityMapper<D, E, F>{
	E toModel(F form);
	D toDto(E entity);
	F toForm(E entity);
	
	List<E> toModel(List<F> dtoList);
    List<D> toDto(List<E> entityList);
}
