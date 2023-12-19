package the.xan.arkenstone.task.tracker.api.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Log4j2
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> exception(Exception exception, WebRequest request) throws Exception {
        log.error("Exception during execution of application", exception);
        return handleException(exception, request);
    }



}
