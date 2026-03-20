package co.edu.eci.blueprints.blueprintsLab4.persistence;

import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;
import co.edu.eci.blueprints.blueprintsLab4.model.Point;

import java.util.List;
import java.util.Set;

public interface BlueprintPersistence {

    void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;

    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;

    Set<Blueprint> getAllBlueprints();

    void addPoints(String author, String name, List<Point> points) throws BlueprintNotFoundException;

    void deleteBlueprint(String author, String name) throws BlueprintNotFoundException;
}
