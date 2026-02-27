package co.edu.eci.blueprints.blueprintsLab4.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;

@Embeddable
@Schema(description = "Punto en un plano 2D")
public record Point(
        @Schema(description = "Coordenada X", example = "10")
        int x, 
        @Schema(description = "Coordenada Y", example = "20")
        int y
) { }
