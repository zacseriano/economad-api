package zacseriano.economadapi.service.competencia;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import zacseriano.economadapi.domain.dto.CompetenciaDto;
import zacseriano.economadapi.domain.form.CompetenciaForm;
import zacseriano.economadapi.domain.mapper.CompetenciaMapper;
import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.repository.CompetenciaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CompetenciaService {
	@Autowired
	private CompetenciaRepository repository;
	@Autowired
	private CompetenciaMapper mapper;
	
	public Competencia carregarOuCriar(CompetenciaForm form) {
		Competencia competencia = repository.findByData(form.getData());
		if (competencia == null) {
			competencia = mapper.toModel(form);
			competencia.setDescricao(getDescricaoData(competencia));
			competencia = repository.save(competencia);
		}
		
		return competencia;
	}
	
	public CompetenciaDto editarSalario(CompetenciaForm form) {
		Competencia competencia = carregarOuCriar(form);
		
		if(form.getSalario() == null) {
			throw new ValidationException("Favor informar o salário.");
		}
		
		competencia.setSalario(form.getSalario());
		competencia = repository.saveAndFlush(competencia);
		return mapper.toDto(competencia);
	}

	public Competencia visualizarPorDescricao(String descricaoCompetencia) {
		Competencia competencia = repository.findByDescricao(descricaoCompetencia);
		
		if (competencia == null) {
			throw new ValidationException(String.format("Competencia com descrição %s não encontrada.", descricaoCompetencia));		
		}
		
		return competencia;
	}
	
	public Competencia buscarOuCriarProximaCompetencia(Competencia competencia) {
		LocalDate proximoMes = competencia.getData().plusMonths(1);
		CompetenciaForm competenciaForm = new CompetenciaForm(proximoMes, new BigDecimal(5500));
		Competencia proximaCompetencia = carregarOuCriar(competenciaForm);		
		return proximaCompetencia;
	}
	
	private String getDescricaoData (Competencia competencia) {
		String mes = competencia.getData().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());		
		return mes + "/" + competencia.getData().getYear();
	}
}