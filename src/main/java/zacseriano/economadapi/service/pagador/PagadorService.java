package zacseriano.economadapi.service.pagador;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public Pagador carregarOuCriar(PagadorForm form) {
		Pagador pagador = pagadorRepository.findByNome(form.getNome());
		
		if (pagador == null) {
			pagador = criar(form);		
		}
		
		return pagador;
	}
}
