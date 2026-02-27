package co.edu.eci.blueprints.blueprintsLab4.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "blueprints")
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Plano (Blueprint) con puntos")
public class Blueprint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "ID único del blueprint", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "Autor del blueprint", example = "john")
    private String author;
    
    @Schema(description = "Nombre del blueprint", example = "house")
    private String name;
    
    @ElementCollection
    @Schema(description = "Lista de puntos que conforman el blueprint")
    private final List<Point> points = new ArrayList<>();

    public Blueprint(String author, String name, List<Point> pts) {
        this.author = author;
        this.name = name;
        if (pts != null) points.addAll(pts);
    }


    public List<Point> getPoints() { return Collections.unmodifiableList(points); }

    public void addPoint(Point p) { points.add(p); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Blueprint bp)) return false;
        return Objects.equals(author, bp.author) && Objects.equals(name, bp.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }
}
