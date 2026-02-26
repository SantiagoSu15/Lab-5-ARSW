package co.edu.eci.blueprints.blueprintsLab4.Util;


import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.blueprintsLab4.persistence.BlueprintPersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BlueprintPersistenceException.class)
    public ResponseEntity<Object> handlePersistent(BlueprintPersistenceException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Persistence",
                        "message", ex.getMessage(),
                        "status", 409
                ));
    }

    @ExceptionHandler(BlueprintNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(BlueprintNotFoundException ex){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", "Not Found",
                        "message", ex.getMessage(),
                        "status", 404
                ));
    }

}
