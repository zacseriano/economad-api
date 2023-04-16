package zacseriano.economadapi.service.despesa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import zacseriano.economadapi.domain.dto.EstatisticasDto;
import zacseriano.economadapi.domain.enums.MesEnum;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.domain.enums.TipoEstatisticaEnum;
import zacseriano.economadapi.domain.form.CompetenciaForm;
import zacseriano.economadapi.domain.form.DespesaForm;
import zacseriano.economadapi.domain.form.EditarDespesaForm;
import zacseriano.economadapi.domain.mapper.DespesaMapper;
import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Origem;
import zacseriano.economadapi.domain.model.Pagador;
import zacseriano.economadapi.repository.DespesaRepository;
import zacseriano.economadapi.service.competencia.CompetenciaService;
import zacseriano.economadapi.service.origem.OrigemService;
import zacseriano.economadapi.service.pagador.PagadorService;
import zacseriano.economadapi.specification.DespesaSpecificationBuilder;

@Service
@Transactional
@RequiredArgsConstructor
public class DespesaService {
	final static int RADIX = 10;
	@Autowired
	private DespesaRepository despesaRepository;
	@Autowired
	private DespesaMapper despesaMapper;
	@Autowired
	private CompetenciaService competenciaService;
	@Autowired
	private OrigemService origemService;
	@Autowired
	private PagadorService pagadorService;
	
	@Value("${salario}")
    private BigDecimal salario;
	
	public Page<Despesa> listar(String descricaoCompetencia, String nomePagador,
			String tipoPagamentoPagador, String nomeOrigem, Pageable paginacao, StatusDespesaEnum statusDespesaEnum) {
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(
				descricaoCompetencia, nomePagador, tipoPagamentoPagador, nomeOrigem, statusDespesaEnum);
		return despesaRepository.findAll(spec, paginacao);
	}
	
	public List<EstatisticasDto> listarEstatisticasPorCompetencia(String descricaoCompetencia, TipoEstatisticaEnum tipoEstatistica) {
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(
				descricaoCompetencia, null, null, null, null);
		List<Despesa> despesas = despesaRepository.findAll(spec);		
		
		List<EstatisticasDto> estatisticas = new ArrayList<>();
		
		Map<String, BigDecimal> mapDespesasEstatisticas = criarMapDespesasEstatisticas(despesas, tipoEstatistica);
		
		for(Entry<String, BigDecimal> entrada: mapDespesasEstatisticas.entrySet()) {
			EstatisticasDto estatisticaDto = new EstatisticasDto(entrada.getKey(), entrada.getValue());
			estatisticas.add(estatisticaDto);
		}
		
		EstatisticasDto.ordenarEstatisticasPorValorDecrescente(estatisticas);
		return estatisticas;
	}
	
	public List<Despesa> visualizar(String nomePagador, String descricaoCompetencia){
		Pagador pagador = pagadorService.visualizarPorNome(nomePagador);
		Competencia competencia = competenciaService.visualizarPorDescricao(descricaoCompetencia);
		List<Despesa> despesas = despesaRepository.findByPagadorAndCompetencia(pagador, competencia);
		
		return despesas;
	}
	
	public Despesa criar(DespesaForm form) {
		Despesa despesa = despesaMapper.toModel(form);
		Competencia competencia = competenciaService.carregarOuCriar(form.getCompetenciaForm());
		despesa.setCompetencia(competencia);
		Origem origem = origemService.carregarOuCriar(form.getOrigemForm());
		despesa.setOrigem(origem);
		Pagador pagador = pagadorService.carregarOuCriar(form.getPagadorForm());
		despesa.setPagador(pagador);				
		
		despesa = despesaRepository.save(despesa);
		
		if(despesa.getParcela() != null) {
			criarDespesasParcelasRestantes(despesa);
		}
		
		return despesa;
	}
	
	public Despesa editar(@Valid EditarDespesaForm editarDespesaForm) {
		Despesa despesa = despesaRepository.findById(editarDespesaForm.getId()).orElseThrow(() -> new ValidationException("Despesa não encontrada com o Id informado."));
		BeanUtils.copyProperties(editarDespesaForm, despesa);
		
		return despesaRepository.save(despesa);
	}
	
	public BigDecimal visualizarTotal(String nomePagador, String descricaoCompetencia) {
		Pagador pagador = pagadorService.visualizarPorNome(nomePagador);
		Competencia competencia = competenciaService.visualizarPorDescricao(descricaoCompetencia);
		List<Despesa> despesas = despesaRepository.findByPagadorAndCompetencia(pagador, competencia);
		BigDecimal total = BigDecimal.ZERO;
		
		for(Despesa despesa : despesas) {
			total = total.add(despesa.getValor());
		}
		
		return total;
	}
	
	public Page<Despesa> pagarDespesas(String descricaoCompetencia, String nomePagador,
			String tipoPagamentoPagador, String nomeOrigem, Pageable paginacao, StatusDespesaEnum statusDespesaEnum) {
		
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(
				descricaoCompetencia, nomePagador, tipoPagamentoPagador, nomeOrigem, statusDespesaEnum);
		Page<Despesa> despesas = despesaRepository.findAll(spec, paginacao);
		
		for(Despesa despesa : despesas) {
			despesa.setStatusDespesaEnum(StatusDespesaEnum.PAGO);
			despesa = despesaRepository.save(despesa);
		}	
		
		return despesas;
	}
	
	private Map<String, BigDecimal> criarMapDespesasEstatisticas(List<Despesa> despesas,
			TipoEstatisticaEnum tipoEstatistica) {
		Map<String, BigDecimal> totalPorPagador = new HashMap<>();
		BigDecimal total = BigDecimal.ZERO;		
		
		if (tipoEstatistica.equals(TipoEstatisticaEnum.PAGADOR)) {
			for (Despesa despesa : despesas) {
				String nomePagadorDespesa = despesa.getPagador().getNome();
				BigDecimal valor = despesa.getValor();
				total = total.add(valor);
				if (totalPorPagador.containsKey(nomePagadorDespesa)) {
					valor = valor.add(totalPorPagador.get(nomePagadorDespesa));
				}

				totalPorPagador.put(nomePagadorDespesa, valor);
			}
		}
		
		if (tipoEstatistica.equals(TipoEstatisticaEnum.ORIGEM)) {
			for (Despesa despesa : despesas) {
				String nomeOrigemDespesa = despesa.getOrigem().getNome();
				BigDecimal valor = despesa.getValor();
				total = total.add(valor);
				if (totalPorPagador.containsKey(nomeOrigemDespesa)) {
					valor = valor.add(totalPorPagador.get(nomeOrigemDespesa));
				}

				totalPorPagador.put(nomeOrigemDespesa, valor);
			}
		}
		
		totalPorPagador.put("TOTAL", total);
		totalPorPagador.put("Dinheiro restante", this.salario.subtract(total));

		return totalPorPagador;

	}
	
	//SÓ FUNCIONA PARA ATÉ 9 PARCELAS, IMPLEMENTAR ALGO MAIS COMPLETO DEPOIS
	private String criarProximaParcela(String parcela) {
		String[] partes = parcela.split("/");
		int parcelaInt = Integer.parseInt(partes[0]);
		parcelaInt++;
		String numeroParcela = Integer.toString(parcelaInt);
		String novaParcela = numeroParcela + "/" + partes[1];

		
		return novaParcela;
	}
	
	private void criarDespesasParcelasRestantes(Despesa despesa) {
		String parcela = despesa.getParcela();
		String[] parts = parcela.split("/");
		int quantidade = Integer.parseInt(parts[1]);
		Competencia competencia = despesa.getCompetencia();
		LocalDate prazo = despesa.getPrazo();
		Despesa despesaAtual = despesa;
		for(int i = quantidade ; i > 1 ; i--) {
			Despesa novaDespesa = new Despesa();
			BeanUtils.copyProperties(despesaAtual, novaDespesa, "id");
			
			competencia = buscarOuCriarProximaCompetencia(competencia);
			novaDespesa.setCompetencia(competencia);
			
			parcela = criarProximaParcela(parcela);
			novaDespesa.setParcela(parcela);
			
			prazo = prazo.plusMonths(1);
			novaDespesa.setPrazo(prazo);
			
			despesaAtual = novaDespesa;
			despesaRepository.save(novaDespesa);
		}				
	}
	
	private Competencia buscarOuCriarProximaCompetencia(Competencia competencia) {
		MesEnum mesAtual = competencia.getMesEnum();
		int ano = competencia.getAno();
		List<MesEnum> meses = Arrays.asList(MesEnum.values());
		Map<Integer, MesEnum> mesesMap = new HashMap<>();
		for(MesEnum mes : meses) {
			mesesMap.put(mes.getNumeroMes(), mes);
		}
		
		int numeroProximoMes = mesAtual.getNumeroMes() + 1;
		
		if (numeroProximoMes > 12) {
			numeroProximoMes = 1;
			ano++;
		}
		
		MesEnum proximoMes = mesesMap.get(numeroProximoMes);
		
		CompetenciaForm competenciaForm = new CompetenciaForm(proximoMes, ano);
		Competencia proximaCompetencia = competenciaService.carregarOuCriar(competenciaForm);
		
		return proximaCompetencia;
	}	
	
}
