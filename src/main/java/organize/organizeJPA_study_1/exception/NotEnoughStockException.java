package organize.organizeJPA_study_1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotEnoughStockException extends RuntimeException {

    private final HttpStatus httpStatus;

    public NotEnoughStockException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
