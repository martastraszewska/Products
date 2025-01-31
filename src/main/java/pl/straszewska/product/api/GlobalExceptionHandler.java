package pl.straszewska.product.api;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.straszewska.product.app.error.InvalidProductRequestException;
import pl.straszewska.product.app.error.ProductNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = ProductNotFoundException.class)
    protected ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex,
                                                                    HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatusCode.valueOf(404))
                .body(new ErrorResponse(ex.getMessage()));

    }

    @ExceptionHandler(value = InvalidProductRequestException.class)
    protected ResponseEntity<Object> handleInvalidProductException(InvalidProductRequestException ex,
                                                                   HttpServletRequest request) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(ex.getMessage()));
    }
}
