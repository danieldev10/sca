package ng.edu.aun.sca;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class CustomErrorController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String handleError(HttpServletRequest request) {
        // Get the HTTP error status code
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            // Handle specific error codes
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404 - Resource not found";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500 - Internal Server Error";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "401 - Unauthorized";
            }
        }
        // Default error message
        return "An error occurred. Please try again later.";
    }

    public String getErrorPath() {
        return PATH;
    }
}
