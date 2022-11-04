package zacseriano.economadapi.domain.mapper;

import org.mapstruct.Mapper;

import zacseriano.economadapi.domain.dto.PagadorDto;
import zacseriano.economadapi.domain.form.PagadorForm;
import zacseriano.economadapi.domain.model.Pagador;

@Mapper(componentModel = "spring")
public interface PagadorMapper extends EntityMapper<PagadorDto, Pagador, PagadorForm> {

}
