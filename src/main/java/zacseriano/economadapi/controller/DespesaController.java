package zacseriano.economadapi.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import zacseriano.economadapi.domain.dto.ConsumoDiarioDto;
import zacseriano.economadapi.domain.dto.DespesaDto;
import zacseriano.economadapi.domain.dto.EstatisticasDto;
import zacseriano.economadapi.domain.enums.TipoEstatisticaEnum;
import zacseriano.economadapi.domain.form.DespesaSimplificadaForm;
import zacseriano.economadapi.domain.mapper.DespesaMapper;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Origem;
import zacseriano.economadapi.service.despesa.DespesaService;
import zacseriano.economadapi.service.origem.OrigemService;
import zacseriano.economadapi.specification.filter.DespesaFilter;

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
			@PageableDefault(size = 20, sort = "data", direction = Sort.Direction.DESC) Pageable paginacao,
			DespesaFilter filtro) {

		Page<Despesa> despesas = despesaService.listar(filtro, paginacao);
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
	
	@GetMapping("/gerar-planilha-mensal")
	public ResponseEntity<byte[]> gerarPlanilhaMensal(@RequestParam(required = false) String descricaoCompetencia) throws IOException {
        byte[] planilhaBytes = despesaService.gerarPlanilhaCompetencia(descricaoCompetencia);
        HttpHeaders headers = new HttpHeaders();
//        String competenciaSemBarra = descricaoCompetencia.replaceAll("/", "");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "planilha_mensal_" + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(planilhaBytes);
    }
	
	@GetMapping("/listar-origens")
	public ResponseEntity<List<String>> listarOrigens() {
		List<Origem> origens = origemService.listarTodos();
		List<String> nomes = origens.stream().map(r -> r.getNome()).toList();
		
		return ResponseEntity.ok(nomes);
	}
	
	@PostMapping("/criar-simplificado")
	public ResponseEntity<DespesaDto> criarSimplificado(@RequestBody @Valid DespesaSimplificadaForm form, UriComponentsBuilder uriBuilder) {
		Despesa despesa = despesaService.criarSimplificado(form);
		DespesaDto despesaDto = despesaMapper.toDto(despesa);
		return ResponseEntity.ok(despesaDto);
	}
	
	@GetMapping("/consumo-diario")
	public ResponseEntity<List<ConsumoDiarioDto>> gerarConsumoDiario(@RequestParam(required = true) LocalDate dataInicio, @RequestParam(required = false) LocalDate dataFim) {
		return ResponseEntity.ok(despesaService.gerarConsumoDiario(dataInicio, dataFim));
	}
	
	@GetMapping("/calcular-idr")
	public ResponseEntity<BigDecimal> calcularIndiceDiarioRelativo(@RequestParam(required = true) LocalDate dataInicio,
			@RequestParam(required = true) LocalDate dataFim) {		
		return ResponseEntity.ok(despesaService.calcularIndiceDiarioRelativo(dataInicio, dataFim));
	}

}
