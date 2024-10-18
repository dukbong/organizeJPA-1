package organize.organizeJPA_study_1.exception.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import organize.organizeJPA_study_1.exception.NotEnoughStockException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> notEnoughStockException(NotEnoughStockException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }

}
