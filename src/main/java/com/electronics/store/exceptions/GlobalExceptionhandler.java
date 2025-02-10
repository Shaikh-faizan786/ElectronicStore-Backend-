package com.electronics.store.exceptions;

import com.electronics.store.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionhandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionhandler.class);

    @ExceptionHandler(ResourcseNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourcseNotFoundException ex) {

        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .success(true)
                .build();
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    // MethodArgumentNotVlidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // <Map<String,Object>> = map class ke andar jo type hoga wo string hoga nad object hoga
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotVlidException(MethodArgumentNotValidException ex) {

        // ex.getBindingResult() = ex hume ye method derha hai uski madad se all error milega
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        //ab all error mila usko has map me store karenge
        Map<String, Object> response = new HashMap<>();
        // ab all erros ki madad se konse filed pe error and konse value pe error arahai hai wo karenge for each ki madads e
        allErrors.stream().forEach(objectError -> {
            // ab objectError ki madad se ek ek karke filed and message ko nikaal sakte hai
            String message = objectError.getDefaultMessage();
            // Filed nikaalne ke liye type cast karna hoga FieldError se
            String field = ((FieldError) objectError).getField();
            // jab filed or message milega to response me daal denge
            response.put(field, message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // bad api exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(BadApiRequestException ex) {
        logger.info("Bad Api Request !!");
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }
}
