package zacseriano.economadapi.service.origem;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import zacseriano.economadapi.domain.form.OrigemForm;
import zacseriano.economadapi.domain.mapper.OrigemMapper;
import zacseriano.economadapi.domain.model.Origem;
import zacseriano.economadapi.repository.OrigemRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class OrigemService {
	@Autowired
	private OrigemRepository origemRepository;
	@Autowired
	private OrigemMapper origemMapper;
	@Autowired
	private OrigemValidator origemValidator;
	
	public List<Origem> listarTodos(){
		return origemRepository.findAll(Sort.by(Direction.ASC, "nome"));
	}
	
	public Origem criar(OrigemForm form) {
		Origem origem = origemMapper.toModel(form);
		origem = origemRepository.save(origem);
		return origem;
	}
	
	public Origem carregarOuCriar(OrigemForm form) {
		Origem origem = origemRepository.findByNome(form.getNome());
		
		if (origem == null) {
			origemValidator.validarForm(form);
			origem = criar(form);			
		}
		
		return origem;
	}
}
