package zacseriano.economadapi.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import zacseriano.economadapi.domain.dto.DespesaDto;
import zacseriano.economadapi.domain.form.DespesaForm;
import zacseriano.economadapi.domain.form.DespesaSimplificadaForm;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.shared.utils.DataUtils;

@Mapper(componentModel = "spring")
public interface DespesaMapper extends EntityMapper<DespesaDto, Despesa, DespesaForm> {
	
    @Mappings({
            @Mapping(target = "data", source = "data", dateFormat = DataUtils.DEFAULT_FORMAT),
            @Mapping(target = "prazo", source = "prazo", dateFormat = DataUtils.DEFAULT_FORMAT),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "deleted", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "competencia", ignore = true),
            @Mapping(target = "origem", ignore = true),
            @Mapping(target = "pagador", ignore = true),
            @Mapping(target = "parcela", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    Despesa toModel(DespesaForm form);
    
    @Mappings({
        @Mapping(target = "data", source = "data", dateFormat = DataUtils.DEFAULT_FORMAT),
        @Mapping(target = "prazo", source = "prazo", dateFormat = DataUtils.DEFAULT_FORMAT),
        @Mapping(target = "createdAt", ignore = true),
        @Mapping(target = "deleted", ignore = true),
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "updatedAt", ignore = true),
        @Mapping(target = "competencia", ignore = true),
        @Mapping(target = "origem", ignore = true),
        @Mapping(target = "pagador", ignore = true),
        @Mapping(target = "parcela", ignore = true),
        @Mapping(target = "status", ignore = true)
    })
    Despesa toModel(DespesaSimplificadaForm form);
//	/"competencia, origem, pagador, parcela, status".
	@Mappings({ 
		@Mapping(target ="origem" , source = "origem.nome"),
		@Mapping(target ="pagador" , source = "pagador.nome"),
		@Mapping(target ="competencia" , source = "competencia.descricao")
	})
	DespesaDto toDto(Despesa entity);
}
