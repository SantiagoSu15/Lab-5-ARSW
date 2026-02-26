package co.edu.eci.blueprints.blueprintsLab4.persistence.Postgres;


import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostgresBlueprintPersistenceRepo  extends JpaRepository<Blueprint, Long> {
    Optional<Blueprint> findByAuthorAndName(String author, String name);
    List<Blueprint> findByAuthor(String author);
}
