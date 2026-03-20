package co.edu.eci.blueprints.blueprintsLab4.services;


import co.edu.eci.blueprints.blueprintsLab4.filters.BlueprintsFilter;
import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;
import co.edu.eci.blueprints.blueprintsLab4.model.Point;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistence;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlueprintsServices {

    private final BlueprintPersistence persistence;
    private final BlueprintsFilter filter;


    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        persistence.saveBlueprint(bp);
    }

    public Set<Blueprint> getAllBlueprints() {
        return persistence.getAllBlueprints();
    }

    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        return persistence.getBlueprintsByAuthor(author);
    }

    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return filter.apply(persistence.getBlueprint(author, name));
    }

    public void addPoints(String author, String name, List<Point> points) throws BlueprintNotFoundException {
        persistence.addPoints(author, name, points);
    }

    public void deleteBlueprint(String author, String name) throws BlueprintNotFoundException {
        persistence.deleteBlueprint(author, name);
    }
}
