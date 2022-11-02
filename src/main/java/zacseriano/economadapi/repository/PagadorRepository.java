package zacseriano.economadapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import zacseriano.economadapi.domain.model.Pagador;

public interface PagadorRepository extends JpaRepository<Pagador, UUID>{
	Pagador findByNome(String nome);
}
