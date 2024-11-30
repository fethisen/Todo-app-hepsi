package com.hepsi.emlak.todo_app.exception;


import com.hepsi.emlak.todo_app.service.dto.response.MessageDto;
import com.hepsi.emlak.todo_app.service.exception.BusinessException;
import com.hepsi.emlak.todo_app.service.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<MessageDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(errorMessage).append(";");
        });

        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessages.toString())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMessage = "Veri formatı hatalı. Lütfen girdilerinizi kontrol ediniz.";
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
    }



    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MessageDto> handleBusinessException(BusinessException ex) {
        log.error("Business exception occurred: {}", ex.getMessage());
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageDto> handleBadCredentialsException(BadCredentialsException ex) {
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message("The username or password you entered is incorrect. Please check your credentials and try again.")
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageDto);

    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<MessageDto> handlForbiddenException(ForbiddenException ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .message("You don't have permission to access this content.")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<MessageDto> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage());
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("An error occured while calling the service. Please ensure that all required parameters are correct and complete.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageDto> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An expected error occured. Please try again.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageDto> handleException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage(), ex);
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An error occured. Please contact your system administrator.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageDto);
    }


}
