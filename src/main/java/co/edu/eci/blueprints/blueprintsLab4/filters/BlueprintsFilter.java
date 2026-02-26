package co.edu.eci.blueprints.blueprintsLab4.filters;


import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;

public interface BlueprintsFilter {
    Blueprint apply(Blueprint bp);
}
