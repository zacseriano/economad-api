package zacseriano.economadapi.service.pagador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import zacseriano.economadapi.domain.form.PagadorForm;
import zacseriano.economadapi.domain.mapper.PagadorMapper;
import zacseriano.economadapi.domain.model.Pagador;
import zacseriano.economadapi.repository.PagadorRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PagadorService {
	@Autowired
	private PagadorRepository pagadorRepository;
	@Autowired
	private PagadorMapper pagadorMapper;
	
	public Pagador criar(PagadorForm form) {
		Pagador pagador = pagadorMapper.toModel(form);
		pagador = pagadorRepository.save(pagador);
		
		return pagador;
	}
	
	public List<Pagador> listarTodos() {	
		return pagadorRepository.findAll();
	}
	
	public Pagador carregarOuCriar(PagadorForm form) {
		Pagador pagador = pagadorRepository.findByNome(form.getNome());
		
		if (pagador == null) {
			pagador = criar(form);		
		}
		
		return pagador;
	}

	public Pagador visualizarPorNome(String nomePagador) {
		Pagador pagador = pagadorRepository.findByNome(nomePagador);
		
		if (pagador == null) {
			throw new ValidationException(String.format("Pagador de nome %s não encontrado.", nomePagador));		
		}
		
		return pagador;
	}
}
