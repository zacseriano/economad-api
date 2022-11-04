package zacseriano.economadapi.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Pagador;

public interface DespesaRepository extends JpaRepository<Despesa, UUID>, JpaSpecificationExecutor<Despesa>{
	List<Despesa> findByPagadorAndCompetencia(Pagador pagador, Competencia competencia);

}
