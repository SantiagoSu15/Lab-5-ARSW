package co.edu.eci.blueprints.blueprintsLab4.controllers;


import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;
import co.edu.eci.blueprints.blueprintsLab4.model.Point;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistenceException;
import co.edu.eci.blueprints.blueprintsLab4.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/blueprints")
@RequiredArgsConstructor
@Tag(name = "Blueprints", description = "API para gestión de planos (blueprints)")
public class BlueprintsAPIController {

    private final BlueprintsServices services;


    @Operation(summary = "Obtener todos los blueprints", 
               description = "Retorna todos los blueprints registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de blueprints obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = Set.class)))
    })
    @GetMapping
    public ResponseEntity<Set<Blueprint>> getAll() {
        return ResponseEntity.ok(services.getAllBlueprints());
    }

    @Operation(summary = "Obtener blueprints por autor", 
               description = "Retorna todos los blueprints creados por un autor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blueprints encontrados",
                    content = @Content(schema = @Schema(implementation = Set.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron blueprints para el autor")
    })
    @GetMapping("/{author}")
    public ResponseEntity<?> byAuthor(
            @Parameter(description = "Nombre del autor", example = "john")
            @PathVariable String author) {
        try {
            return ResponseEntity.ok(services.getBlueprintsByAuthor(author));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener blueprint específico", 
               description = "Retorna un blueprint específico por autor y nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blueprint encontrado",
                    content = @Content(schema = @Schema(implementation = Blueprint.class))),
            @ApiResponse(responseCode = "404", description = "Blueprint no encontrado")
    })
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<?> byAuthorAndName(
            @Parameter(description = "Nombre del autor", example = "john")
            @PathVariable String author, 
            @Parameter(description = "Nombre del blueprint", example = "house")
            @PathVariable String bpname) {
        try {
            return ResponseEntity.ok(services.getBlueprint(author, bpname));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Crear un nuevo blueprint", 
               description = "Registra un nuevo blueprint en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Blueprint creado exitosamente"),
            @ApiResponse(responseCode = "403", description = "El blueprint ya existe o error de validación")
    })
    @PostMapping
    @SecurityRequirement(name = "bearer-jwt")
    @PreAuthorize("hasAuthority('SCOPE_blueprints.write')")
    public ResponseEntity<?> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (BlueprintPersistenceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Agregar punto a un blueprint", 
               description = "Agrega un nuevo punto a un blueprint existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Punto agregado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Blueprint no encontrado")
    })
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<?> addPoint(
            @Parameter(description = "Nombre del autor", example = "john")
            @PathVariable String author, 
            @Parameter(description = "Nombre del blueprint", example = "house")
            @PathVariable String bpname,
            @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @Schema(description = "Solicitud para crear un nuevo blueprint")
    public record NewBlueprintRequest(
            @Schema(description = "Autor del blueprint", example = "john")
            @NotBlank String author,
            @Schema(description = "Nombre del blueprint", example = "house")
            @NotBlank String name,
            @Schema(description = "Lista de puntos del blueprint")
            @Valid java.util.List<Point> points
    ) { }
}
