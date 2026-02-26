package co.edu.eci.blueprints.blueprintsLab4.persistence;

import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;

import java.util.Set;

public interface BlueprintPersistence {

    void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;

    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;

    Set<Blueprint> getAllBlueprints();

    void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException;
}
