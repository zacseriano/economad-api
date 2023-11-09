package zacseriano.economadapi.service.despesa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import zacseriano.economadapi.domain.dto.ConsumoDiarioDto;
import zacseriano.economadapi.domain.dto.EstatisticasDto;
import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.domain.enums.TipoEstatisticaEnum;
import zacseriano.economadapi.domain.form.CompetenciaForm;
import zacseriano.economadapi.domain.form.DespesaForm;
import zacseriano.economadapi.domain.form.DespesaSimplificadaForm;
import zacseriano.economadapi.domain.mapper.DespesaMapper;
import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Origem;
import zacseriano.economadapi.domain.model.Pagador;
import zacseriano.economadapi.repository.DespesaRepository;
import zacseriano.economadapi.service.competencia.CompetenciaService;
import zacseriano.economadapi.service.origem.OrigemService;
import zacseriano.economadapi.service.pagador.PagadorService;
import zacseriano.economadapi.specification.builder.DespesaSpecificationBuilder;
import zacseriano.economadapi.specification.filter.DespesaFilter;


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
	
	public Page<Despesa> listar(DespesaFilter filter, Pageable paginacao) {		
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(filter);
		return despesaRepository.findAll(spec, paginacao);
	}
	
	public List<EstatisticasDto> listarEstatisticasPorCompetencia(String descricaoCompetencia, TipoEstatisticaEnum tipoEstatistica) {
		DespesaFilter filtro = DespesaFilter.builder().descricaoCompetencia(descricaoCompetencia).build();
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(filtro);
		Competencia competencia = competenciaService.visualizarPorDescricao(descricaoCompetencia);
		if(competencia.getSalario() == null) {
			throw new ValidationException(String.format("Favor, cadastrar o salário da Competência: %s", descricaoCompetencia));
		}
		List<Despesa> despesas = despesaRepository.findAll(spec);		
		Map<String, BigDecimal> mapDespesasEstatisticas = criarMapDespesasEstatisticas(despesas, tipoEstatistica, competencia.getSalario());
		List<EstatisticasDto> estatisticas = mapDespesasEstatisticas.entrySet().stream()
				.map(entry -> new EstatisticasDto(entry.getKey(), entry.getValue())).collect(Collectors.toList());
		estatisticas.sort((e1, e2) -> e2.getTotal().compareTo(e1.getTotal()));
		return estatisticas;
	}
	
	public byte[] gerarPlanilhaCompetencia(String descricaoCompetencia) {
		DespesaFilter filtro = DespesaFilter.builder().descricaoCompetencia(descricaoCompetencia).build();
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(filtro);
		Competencia competencia = competenciaService.visualizarPorDescricao(descricaoCompetencia);
		if(competencia.getSalario() == null) {
			throw new ValidationException(String.format("Favor, cadastrar o salário da Competência: %s", descricaoCompetencia));
		}
		List<Despesa> despesas = despesaRepository.findAll(spec);		
		return generateWorksheet(despesas);
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
	
	public Despesa criarSimplificado(DespesaSimplificadaForm form) {
		Despesa despesa = despesaMapper.toModel(form);
		LocalDate competenciaDate = despesa.getPrazo().withDayOfMonth(1);
		Competencia competencia = competenciaService.carregarOuCriar(new CompetenciaForm(competenciaDate, new BigDecimal(5500)));
		despesa.setCompetencia(competencia);
		Origem origem = origemService.visualizarPorNomeOuCriar(form.getNomeOrigem());
		despesa.setOrigem(origem);
		Pagador pagador = pagadorService.visualizarPorNome(form.getNomePagador());
		despesa.setPagador(pagador);
		StatusDespesaEnum status = form.getPago() ? StatusDespesaEnum.PAGO : StatusDespesaEnum.NÃO_PAGO;
		despesa.setStatus(status);
		despesa.setParcela(criarParcelaSimplificada(form.getNumeroParcelas()));		
		if(form.getNumeroParcelas() > 1) {
			criarDespesasParcelasRestantes(despesa);
		}		
		despesa = despesaRepository.save(despesa);
		return despesa;
	}
	
	public BigDecimal calcularIndiceDiarioRelativo(LocalDate dataInicio, LocalDate dataFim) {
		DespesaFilter filtro = DespesaFilter.builder().dataInicio(dataInicio).dataFim(dataFim).build();
		BigDecimal indice = BigDecimal.ZERO;
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(filtro);
		List<Despesa> despesas = despesaRepository.findAll(spec);
		if(!despesas.isEmpty()) {
			BigDecimal numeroDias = new BigDecimal(ChronoUnit.DAYS.between(dataInicio, dataFim));
			BigDecimal totalValor = despesas.stream().map(Despesa::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
			indice = totalValor.divide(numeroDias, 2, RoundingMode.CEILING);
		}		
		return indice;
	}
	
	private String criarParcelaSimplificada(Integer numeroParcelas) {
		StringBuilder sb = new StringBuilder();
	    sb.append("1/").append(numeroParcelas);
		return sb.toString();
	}

	private Map<String, BigDecimal> criarMapDespesasEstatisticas(List<Despesa> despesas, TipoEstatisticaEnum tipoEstatistica, 
			BigDecimal salario) {
		
		Map<String, BigDecimal> totalPorPagador = new HashMap<>();
		Competencia competenciaAtual = despesas.get(0).getCompetencia();
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
//		BigDecimal valorDespesasPagas = despesaRepository.sumTotalByStatusAndCompetencia(StatusDespesaEnum.PAGO, competenciaAtual.getData());		
		totalPorPagador.put("TOTAL", total);
		totalPorPagador.put("Dinheiro restante LÍQUIDO PREVISTO", salario.subtract(total));
//		totalPorPagador.put("Dinheiro restante ATUAL", salario.subtract(valorDespesasPagas == null ? BigDecimal.ZERO : valorDespesasPagas));		
		return totalPorPagador;
	}

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
			competencia = competenciaService.buscarOuCriarProximaCompetencia(competencia);
			novaDespesa.setCompetencia(competencia);
			parcela = criarProximaParcela(parcela);
			novaDespesa.setParcela(parcela);
			prazo = prazo.plusMonths(1);
			novaDespesa.setPrazo(prazo);	
			despesaAtual = novaDespesa;
			despesaRepository.save(novaDespesa);
		}				
	}
	
	private byte[] gerarPlanilhaExcel(List<Despesa> despesas) {
	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Planilha1");

	        Row headerRow = sheet.createRow(0);
	        headerRow.createCell(0).setCellValue("Origem");
	        headerRow.createCell(1).setCellValue("Valor");
	        headerRow.createCell(2).setCellValue("Data");
	        headerRow.createCell(3).setCellValue("Status");
	        headerRow.createCell(4).setCellValue("Descrição");

	        int rowIndex = 1;
	        for (Despesa despesa : despesas) {
	            Row dataRow = sheet.createRow(rowIndex);
	            dataRow.createCell(0).setCellValue(despesa.getOrigem().getNome());
	            dataRow.createCell(1).setCellValue(Double.parseDouble(despesa.getValor().toString())); // Converter para double
	            dataRow.createCell(2).setCellValue(despesa.getData().toString());
	            dataRow.createCell(3).setCellValue(despesa.getStatus().toString());
	            dataRow.createCell(4).setCellValue(despesa.getDescricao());
	            rowIndex++;
	        }

	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            workbook.write(outputStream);
	            return outputStream.toByteArray();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	private byte[] generateWorksheet(List<Despesa> expenses) {
	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Worksheet");
	        Integer cellIndex = Integer.valueOf(0);
	        Row headerRow = sheet.createRow(0);
	        headerRow.createCell(cellIndex++).setCellValue("Date");
	        headerRow.createCell(cellIndex++).setCellValue("Origin");
	        headerRow.createCell(cellIndex++).setCellValue("Description");
	        headerRow.createCell(cellIndex++).setCellValue("Value");
	        headerRow.createCell(cellIndex++).setCellValue("Payment Type");
	        headerRow.createCell(cellIndex++).setCellValue("Deadline");
	        headerRow.createCell(cellIndex++).setCellValue("Installment");
	        headerRow.createCell(cellIndex++).setCellValue("Installments Number");
	        headerRow.createCell(cellIndex++).setCellValue("Payment Types");
	        headerRow.createCell(cellIndex++).setCellValue("Type");
	        headerRow.createCell(cellIndex++).setCellValue("Billing Date");
	        headerRow.createCell(cellIndex++).setCellValue("Salary");
	        List<Pagador> payments = pagadorService.listarTodos();
	        Integer rowsNumber = payments.size() > expenses.size() ? payments.size() : expenses.size();
	        createRows(rowsNumber, sheet);
	        insertExpensesData(expenses, sheet);
	        insertPaymentTypesData(payments, sheet);
	        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
	            workbook.write(outputStream);
	            return outputStream.toByteArray();
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	private void createRows(Integer rowsNumber, Sheet sheet) {
		for (int i = 1; i <= rowsNumber; i++) {
            sheet.createRow(i);
        }		
	}

	private void insertPaymentTypesData(List<Pagador> paymentTypes, Sheet sheet) {
		int rowIndex = 1;
		int cellIndex = 8;
		for(Pagador paymentType : paymentTypes) {
			cellIndex = 8;
            Row dataRow = sheet.getRow(rowIndex);
			dataRow.createCell(cellIndex++).setCellValue(paymentType.getNome());
        	dataRow.createCell(cellIndex++).setCellValue(paymentType.getTipoPagamentoEnum().getDescricao());
        	dataRow.createCell(cellIndex++).setCellValue(gerarDiaFatura(paymentType.getNome()));
        	if(rowIndex == 1) {
        		dataRow.createCell(cellIndex).setCellValue(5500);
        	}
        	rowIndex++;
		}		
	}

	private Integer gerarDiaFatura(String nome) {
		switch(nome) {
		case "Nubank":
			return 27;
		case "Inter":
			return 20;
		case "BB":
			return 0;
		case "PicPay":
			return 10;
		default:
			return 0;
		}
	}

	private void insertExpensesData(List<Despesa> expenses, Sheet sheet) {
		int rowIndex = 1;
		Integer cellIndex;
        for (Despesa expense : expenses) {
        	cellIndex = 0;
            Row dataRow = sheet.getRow(rowIndex);
            dataRow.createCell(cellIndex++).setCellValue(expense.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            dataRow.createCell(cellIndex++).setCellValue(expense.getOrigem().getNome());
            dataRow.createCell(cellIndex++).setCellValue(expense.getDescricao());
            dataRow.createCell(cellIndex++).setCellValue(Double.parseDouble(expense.getValor().toString()));
            dataRow.createCell(cellIndex++).setCellValue(expense.getPagador().getNome());
            dataRow.createCell(cellIndex++).setCellValue(expense.getPrazo().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            List<Integer> numerosParcelas = obterNumeros(expense.getParcela());
            dataRow.createCell(cellIndex++).setCellValue(numerosParcelas.get(0));
            dataRow.createCell(cellIndex++).setCellValue(numerosParcelas.get(1));
            rowIndex++;
        }		
	}
	
	public static List<Integer> obterNumeros(String input) {
		if(input == null) {
			return Arrays.asList(1,1);
		}
        String[] partes = input.split("/");
        List<Integer> numeros = new ArrayList<>();

        // Converte as partes para inteiros e adiciona à lista
        numeros.add(Integer.parseInt(partes[0]));
        numeros.add(Integer.parseInt(partes[1]));

        return numeros;
    }

	public List<ConsumoDiarioDto> gerarConsumoDiario(LocalDate dataInicio, LocalDate dataFim) {
		if(dataFim == null) {
			dataFim = LocalDate.now();
		}		
		DespesaFilter filter = DespesaFilter.builder().dataInicio(dataInicio).dataFim(dataFim).build();
		Specification<Despesa> spec = DespesaSpecificationBuilder.builder(filter);
		List<Despesa> despesas = despesaRepository.findAll(spec);
		Map<LocalDate, BigDecimal> mapDiaTotal = new HashMap<>();
		for(Despesa despesa : despesas) {
			BigDecimal total = BigDecimal.ZERO;
			if(mapDiaTotal.containsKey(despesa.getData())) {
				total = mapDiaTotal.get(despesa.getData());				
			}
			total = total.add(despesa.getValor());
			mapDiaTotal.put(despesa.getData(), total);
		}
		List<ConsumoDiarioDto> consumos = new ArrayList<>();
		for(Entry<LocalDate,BigDecimal> entry : mapDiaTotal.entrySet()) {
			ConsumoDiarioDto consumo = ConsumoDiarioDto.builder().dia(entry.getKey())
					.total(entry.getValue()).build();
			consumos.add(consumo);
		}
		Comparator<ConsumoDiarioDto> comparadorPorDia = Comparator.comparing(ConsumoDiarioDto::getDia);
        Collections.sort(consumos, comparadorPorDia);
		return consumos;
	}

}