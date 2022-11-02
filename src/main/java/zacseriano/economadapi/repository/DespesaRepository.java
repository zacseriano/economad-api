package zacseriano.economadapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import zacseriano.economadapi.domain.model.Despesa;

public interface DespesaRepository extends JpaRepository<Despesa, UUID>{

}
