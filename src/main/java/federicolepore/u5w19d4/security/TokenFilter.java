package federicolepore.u5w19d4.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;

    public TokenFilter(TokenTools tokenTools) {
        this.tokenTools = tokenTools;
    }


    // questo è un metodo che viene chiamato ad ogni chiamata http, sarà quindi lui l'incaricato della verifica del token
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1 - per prima cosa controlliamo che la richiesta abbia l'Header Authorization e che contenga il token nel formato "Bearer token". In caso contrario lanciamo un errore
        String authHeader = request.getHeader("Authorization");
        // request.getHeader() serve per leggere il valore di un header HTTP. Con "Authorization" possiamo recuperare il token. N.B. se l'header non esiste ritorna NULL, per questo bisogna fare un controllo
        if (authHeader == null || !authHeader.startsWith("Bearer"))
            throw new UnavailableException("Inserire il token nell'authorization header");

        // 2 - Adesso dobbiamo estrarre il token dall'header
        String accessToken = authHeader.replace("Bearer", "");
        // In questo modo prendiamo la stringa authHeader ("Bearer token") e sostituiamo Bearer con una stringa vuota, in questo modo la stringa che ne risulta sarà = "Bearer"

        // 3 - Verifichiamo la firma del token e che non sia scaduto con il metodo creato in TokenTools
        tokenTools.verifyToken(accessToken);

        // 4 - passiamo il controllo avanti con questo comando
        filterChain.doFilter(request, response);

    }

    // serve per saltare autenticazione JWT su endpoint pubblici ed evitare controlli su login e registrazione
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher() // classe di spring che serve per confrontare URL usando dei pattern
                // .match() confronta il path della richiesta con il patterne e restituisce true/false
                .match("/auth/**", // prende in esame auth+qualsiasi cosa ci sia dopo (ci evita di farne più specifici
                        // restituisce il percorso della richiesta HTTP
                        request.getServletPath()); //
    }
}
