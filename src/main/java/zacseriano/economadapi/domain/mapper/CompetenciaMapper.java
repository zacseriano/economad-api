package zacseriano.economadapi.domain.mapper;

import org.mapstruct.Mapper;

import zacseriano.economadapi.domain.dto.CompetenciaDto;
import zacseriano.economadapi.domain.form.CompetenciaForm;
import zacseriano.economadapi.domain.model.Competencia;

@Mapper(componentModel = "spring")
public interface CompetenciaMapper extends EntityMapper<CompetenciaDto, Competencia, CompetenciaForm> {

}
