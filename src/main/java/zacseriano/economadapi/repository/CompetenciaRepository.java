package zacseriano.economadapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import zacseriano.economadapi.domain.enums.MesEnum;
import zacseriano.economadapi.domain.model.Competencia;

public interface CompetenciaRepository extends JpaRepository<Competencia, UUID>{
	Competencia findByMesEnumAndAno(MesEnum mesEnum, int ano);
}
