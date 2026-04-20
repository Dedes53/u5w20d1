package federicolepore.u5w19d4.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(UUID id) {
        super("Non è stato possibile trovare una corrispondenza con l'id: " + id);
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
