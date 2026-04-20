package federicolepore.u5w19d4.security;

import federicolepore.u5w19d4.entities.Author;
import federicolepore.u5w19d4.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenTools {

//    QUESTO COMPONENT SERVE A GENERARE E LEGGERE I TOKEN
//    COME PRIMA COSA GLI PASSIAMO COME VARIABILE IL CODICE SEGRETO,
//    DOPODICHE CREEREMO DUE METODI IN CUI USARE I COMANDI Jwts.builder() E Jwts.parser()


    private final String secret;

    public TokenTools(@Value("${jwt_secret}") String secret) {
        this.secret = secret;
    }

    // creiamo il token
    public String generateToken(Author author) {
        // Come parametro dobbiamo passargli un entità, tendenzialmente l'utente, in questo caso identificata come Author, perché per generare il token dovremo passare il suo UUID per associarlo.
        // Una volta chiamato il builder dovremo dichiarare la data di emissione(1), quella di scadenza(2), la proprietà del token(3), passare il secret(4) e infine concludere con il comando .compact()(5)
        return Jwts.builder() //(0)
                .issuedAt(new Date(System.currentTimeMillis())) //(1)
                .expiration(new Date(System.currentTimeMillis() * 1000 + 60 + 60 + 24 + 7)) //(2)
                .subject(String.valueOf(author.getId())) //(3)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) //(4)
                .compact(); //(5)
        // .getBytes() trasforma una stringa in un array di byte
        // .Keys.hmacShaKeyFor() crea una chiave crittografata con HMAC-SHA a partire dall'argomento che gli passiamo che gli abbiamo passato.

    }


    // verifichiamo il toke
    public void verifyToken(String token) {
        try {
            Jwts.parser() // crea oggetto parser JWT che può leggere il token
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes())) // passiamo la chiave per verificare la firma del token
                    .build() // costruisce il parser con la configurazione che gli abbiamo passato
                    .parse(token); // legge il token, verifica la firma e ne restituisce il contenuto
        } catch (Exception ex) {
            throw new UnauthorizedException("Ci sono problemi con il token, riprova il login");
        }
    }
}
