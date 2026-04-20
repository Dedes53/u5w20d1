package federicolepore.u5w19d4.services;

import federicolepore.u5w19d4.entities.Author;
import federicolepore.u5w19d4.exceptions.NotFoundException;
import federicolepore.u5w19d4.exceptions.UnauthorizedException;
import federicolepore.u5w19d4.payloads.LoginDTO;
import federicolepore.u5w19d4.security.TokenTools;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthorService authorService;
    private final TokenTools tokenTools;


    public AuthenticationService(AuthorService authorService, TokenTools tokenTools) {
        this.authorService = authorService;
        this.tokenTools = tokenTools;
    }


    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        // 1 - controllo le credenziale, quindi se esiste un utente con quella email
        try {
            Author found = this.authorService.findByEmail(body.email());
            // Author non ha una password, per cui non è possibile fare nessun controllo
//            if (found.getPassword().equals(body.password())) {
//                // 2. Se credenziali OK -> Generiamo Token e ritorniamolo
            return this.tokenTools.generateToken(found);
//        } else {
//            // 3. Altrimenti -> Error
//            throw new UnauthorizedException("Credenziali errate");
//        }
        } catch (NotFoundException ex) {
            throw new UnauthorizedException("Credenziali errate");
        }
    }

}
