package co.edu.eci.blueprints.blueprintsLab4.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Point(int x, int y) { }
