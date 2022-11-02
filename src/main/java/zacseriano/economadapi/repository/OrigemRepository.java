package zacseriano.economadapi.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import zacseriano.economadapi.domain.model.Origem;

public interface OrigemRepository extends JpaRepository<Origem, UUID>{

}
