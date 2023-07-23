package zacseriano.economadapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import zacseriano.economadapi.domain.enums.StatusDespesaEnum;
import zacseriano.economadapi.domain.model.Competencia;
import zacseriano.economadapi.domain.model.Despesa;
import zacseriano.economadapi.domain.model.Pagador;

public interface DespesaRepository extends JpaRepository<Despesa, UUID>, JpaSpecificationExecutor<Despesa>{
	List<Despesa> findByPagadorAndCompetencia(Pagador pagador, Competencia competencia);
	@Query("SELECT SUM(d.valor) FROM Despesa d JOIN d.competencia c WHERE d.status = :status AND c.data = :dataCompetencia")
    BigDecimal sumTotalByStatusAndCompetencia(@Param("status") StatusDespesaEnum status, @Param("dataCompetencia") LocalDate dataCompetencia);
}
