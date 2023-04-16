package zacseriano.economadapi.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import zacseriano.economadapi.domain.dto.DespesaDto;
import zacseriano.economadapi.domain.dto.EstatisticasDto;
import zacseriano.economadapi.domain.enums.TipoEstatisticaEnum;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.domain.form.DespesaForm;
import zacseriano.economadapi.domain.form.EditarDespesaForm;
import zacseriano.economadapi.domain.mapper.DespesaMapper;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Origem;
import zacseriano.economadapi.service.despesa.DespesaService;
import zacseriano.economadapi.service.origem.OrigemService;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
	@Autowired
	private DespesaService despesaService;
	@Autowired
	private DespesaMapper despesaMapper;
	@Autowired
	private OrigemService origemService;

	@GetMapping
	public ResponseEntity<Page<DespesaDto>> listar(
			@PageableDefault(size = 50, sort = "data", direction = Sort.Direction.DESC) Pageable paginacao,
			@RequestParam(required = false) String descricaoCompetencia,
			@RequestParam(required = false) String nomePagador,
			@RequestParam(required = false) String tipoPagamentoPagador,
			@RequestParam(required = false) String nomeOrigem,
			@RequestParam(required = false) StatusDespesaEnum statusDespesaEnum) {

		Page<Despesa> despesas = despesaService.listar(descricaoCompetencia, nomePagador, tipoPagamentoPagador,
				nomeOrigem, paginacao, statusDespesaEnum);
		Page<DespesaDto> despesasDto = despesas.map(this.despesaMapper::toDto);

		return ResponseEntity.ok(despesasDto);
	}
	
	@PutMapping("/editar")
	public ResponseEntity<DespesaDto> editar(@RequestBody @Valid EditarDespesaForm editarDespesaForm) {
		Despesa despesa = despesaService.editar(editarDespesaForm);
		return ResponseEntity.ok(this.despesaMapper.toDto(despesa));
	}
	
	@PutMapping
	public ResponseEntity<Page<DespesaDto>> pagarDespesas(
			@PageableDefault(size = 50, sort = "data", direction = Sort.Direction.DESC) Pageable paginacao,
			@RequestParam(required = false) String descricaoCompetencia,
			@RequestParam(required = false) String nomePagador,
			@RequestParam(required = false) String tipoPagamentoPagador,
			@RequestParam(required = false) String nomeOrigem,
			@RequestParam(required = false) StatusDespesaEnum statusDespesaEnum) {
		
		Page<Despesa> despesas = despesaService.pagarDespesas(descricaoCompetencia, nomePagador, tipoPagamentoPagador,
				nomeOrigem, paginacao, statusDespesaEnum);
		
		Page<DespesaDto> despesasDto = despesas.map(this.despesaMapper::toDto);

		return ResponseEntity.ok(despesasDto);
	}
	

	@GetMapping("/estatisticas")
	public ResponseEntity<List<EstatisticasDto>> estatisticas(
			@RequestParam(required = true) String descricaoCompetencia,
			@RequestParam(required = true) TipoEstatisticaEnum tipoEstatistica) {

		List<EstatisticasDto> estatisticasDto = despesaService.listarEstatisticasPorCompetencia(descricaoCompetencia, tipoEstatistica);

		return ResponseEntity.ok(estatisticasDto);
	}

	@GetMapping("/total")
	public ResponseEntity<BigDecimal> visualizarTotal(@RequestParam String pagador, @RequestParam String competencia) {
		BigDecimal total = despesaService.visualizarTotal(pagador, competencia);

		return ResponseEntity.ok(total);
	}
	
	@GetMapping("/listaOrigem")
	public ResponseEntity<List<String>> listarOrigens(){
		List<Origem> origens = origemService.listarTodos();
		List<String> nomes = origens.stream().map(r -> r.getNome()).toList();
		
		return ResponseEntity.ok(nomes);
	}
	@PostMapping
	public ResponseEntity<DespesaDto> criar(@RequestBody @Valid DespesaForm form, UriComponentsBuilder uriBuilder) {
		Despesa despesa = despesaService.criar(form);
		DespesaDto despesaDto = despesaMapper.toDto(despesa);

		return ResponseEntity.created(URI.create("/" + despesaDto.getId())).body(despesaDto);
	}

}
