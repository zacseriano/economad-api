package zacseriano.economadapi.domain.mapper;

import org.mapstruct.Mapper;

import zacseriano.economadapi.domain.dto.OrigemDto;
import zacseriano.economadapi.domain.form.OrigemForm;
import zacseriano.economadapi.domain.model.Origem;

@Mapper(componentModel = "spring")
public interface OrigemMapper extends EntityMapper<OrigemDto, Origem, OrigemForm> {

}
