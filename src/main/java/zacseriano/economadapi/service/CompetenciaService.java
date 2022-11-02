package zacseriano.economadapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
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
	
	public Competencia carregarOuCriarCompetencia(CompetenciaForm form) {
		Competencia competencia = repository.findByMesEnumAndAno(form.getMesEnum(), form.getAno());
		if (competencia == null) {
			competencia = mapper.toModel(form);
			competencia.setDescricao(competencia.getMesEnum().toString() + "/" + competencia.getAno());
			competencia = repository.save(competencia);
		}
		
		return competencia;
	}
}
