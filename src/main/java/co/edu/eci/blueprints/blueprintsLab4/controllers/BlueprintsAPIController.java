package co.edu.eci.blueprints.blueprintsLab4.controllers;


import co.edu.eci.blueprints.blueprintsLab4.model.Blueprint;
import co.edu.eci.blueprints.blueprintsLab4.model.Point;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistenceException;
import co.edu.eci.blueprints.blueprintsLab4.services.BlueprintsServices;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/blueprints")
@RequiredArgsConstructor

public class BlueprintsAPIController {

    private final BlueprintsServices services;


    // GET /blueprints
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
    public ResponseEntity<Set<Blueprint>> getAll() {
        return ResponseEntity.ok(services.getAllBlueprints());
    }

    // GET /blueprints/{author}
    @GetMapping("/{author}")
    @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
    public ResponseEntity<?> byAuthor(@PathVariable String author) {
        try {
            return ResponseEntity.ok(services.getBlueprintsByAuthor(author));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /blueprints/{author}/{bpname}
    @GetMapping("/{author}/{bpname}")
    @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
    public ResponseEntity<?> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            return ResponseEntity.ok(services.getBlueprint(author, bpname));
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // POST /blueprints
    @PostMapping
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

    // PUT /blueprints/{author}/{bpname}/points
    @PutMapping("/{author}/{bpname}/points")
    @PreAuthorize("hasAuthority('SCOPE_blueprints.write')")
    public ResponseEntity<?> addPoint(@PathVariable String author, @PathVariable String bpname,
                                      @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } catch (BlueprintNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }
}
