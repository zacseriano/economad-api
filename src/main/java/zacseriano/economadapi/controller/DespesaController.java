package zacseriano.economadapi.controller;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import zacseriano.economadapi.domain.dto.DespesaDto;
import zacseriano.economadapi.domain.form.DespesaForm;
import zacseriano.economadapi.domain.mapper.DespesaMapper;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.service.despesa.DespesaService;

@RestController
@RequestMapping("/despesas")
public class DespesaController {
	@Autowired
	private DespesaService despesaService;
	@Autowired
	private DespesaMapper despesaMapper;
	
	@GetMapping
	public ResponseEntity<List<DespesaDto>> visualizar(@RequestParam String pagador, @RequestParam String competencia){
		List<Despesa> despesas = despesaService.visualizar(pagador, competencia);
		List<DespesaDto> despesasDto = despesaMapper.toDto(despesas);
		
		return ResponseEntity.ok(despesasDto);
	}
	
	@GetMapping("/total")
	public ResponseEntity<BigDecimal> visualizarTotal(@RequestParam String pagador, @RequestParam String competencia){
		BigDecimal total = despesaService.visualizarTotal(pagador, competencia);
		
		return ResponseEntity.ok(total);
	}
	
	@PostMapping
    public ResponseEntity<DespesaDto> criar(@RequestBody @Valid DespesaForm form,
			UriComponentsBuilder uriBuilder) {
		Despesa despesa = despesaService.criar(form);
		DespesaDto despesaDto = despesaMapper.toDto(despesa);
		
        return ResponseEntity
                .created(URI.create("/"+despesaDto.getId()))
                .body(despesaDto);
    }

}
