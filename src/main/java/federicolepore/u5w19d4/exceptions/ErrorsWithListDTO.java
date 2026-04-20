package federicolepore.u5w19d4.exceptions;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsWithListDTO(String message, LocalDateTime timeStamp, List<String> errors) {
}
