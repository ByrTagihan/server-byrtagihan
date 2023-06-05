package serverbyrtagihan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import serverbyrtagihan.response.ResponseHelper;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFound(NotFoundException notFoundException) {
        return ResponseHelper.error(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<?> InternalError(InternalErrorException internalErrorException) {
        return ResponseHelper.error(internalErrorException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailCondition.class)
    public ResponseEntity<?> EmailCondition(EmailCondition emailCondition) {
        return ResponseHelper.error(emailCondition.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
