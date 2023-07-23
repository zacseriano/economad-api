package zacseriano.economadapi.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import zacseriano.economadapi.domain.model.Competencia;

public interface CompetenciaRepository extends JpaRepository<Competencia, UUID>{
	Competencia findByData(LocalDate data);
	Competencia findByDescricao(String descricao);
}
