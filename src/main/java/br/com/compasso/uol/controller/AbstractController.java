package br.com.compasso.uol.controller;

import br.com.compasso.uol.entity.ResponseErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public class AbstractController {

    public ResponseErrorModel formatBindingErrors(Errors errors, HttpStatus httpStatus) {
        return ResponseErrorModel.builder()
                .statusCode(httpStatus.value())
                .message(createMensageError(errors.getAllErrors()))
                .build();
    }

    public ResponseErrorModel formatBindingErrors(String mensagem, HttpStatus httpStatus) {
        return ResponseErrorModel.builder()
                .statusCode(httpStatus.value())
                .message(mensagem)
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseErrorModel handleValidationExceptions(MethodArgumentNotValidException ex) {
        return formatBindingErrors(ex.getBindingResult(), HttpStatus.BAD_REQUEST);
    }


    private String createMensageError(List<ObjectError> allErrors) {
        StringBuilder msg = new StringBuilder();
        allErrors.stream().forEach(
                objectError -> msg.append(objectError.getDefaultMessage())
        );
        return msg.toString();
    }
}
