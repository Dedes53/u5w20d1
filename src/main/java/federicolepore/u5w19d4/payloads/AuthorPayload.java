package federicolepore.u5w19d4.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@ToString
public class AuthorPayload {

    private String nome;
    private String cognome;
    private String email;
    private LocalDate dataDiNascita;

}
