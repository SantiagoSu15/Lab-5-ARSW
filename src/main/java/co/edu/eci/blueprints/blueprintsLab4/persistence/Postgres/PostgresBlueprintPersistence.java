package co.edu.eci.blueprints.blueprintsLab4.persistence.Postgres;

import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;
import co.edu.eci.blueprints.blueprintsLab4.model.Point;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistence;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistenceException;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@Profile("postgresBD")
@AllArgsConstructor
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    private final PostgresBlueprintPersistenceRepo blueprintRepository;


    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (bp.getId() != null && blueprintRepository.existsById(bp.getId())) {
            throw new BlueprintPersistenceException("Blueprint already exists: " + bp.getId());
        }
        blueprintRepository.save(bp);
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return  blueprintRepository.findByAuthorAndName(author,name).orElseThrow(() -> new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name)));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        List<Blueprint> blueprints = blueprintRepository.findByAuthor(author);
        if (blueprints.isEmpty()) throw new BlueprintNotFoundException("No blueprints for author: " + author);
        return new HashSet<>(blueprints);
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        List<Blueprint> blueprints = blueprintRepository.findAll();
        return new HashSet<>(blueprints);
    }

    @Override
    public void addPoints(String author, String name, List<Point> points) throws BlueprintNotFoundException {
        Blueprint blue = blueprintRepository.findByAuthorAndName(author, name)
                .orElseThrow(() -> new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name)));
        points.forEach(blue::addPoint);
        blueprintRepository.save(blue);
    }

    @Override
    public void deleteBlueprint(String author, String name) throws BlueprintNotFoundException{
        Blueprint blue =  blueprintRepository.findByAuthorAndName(author,name).orElseThrow(() -> new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name)));
        blueprintRepository.delete(blue);
    }
}
