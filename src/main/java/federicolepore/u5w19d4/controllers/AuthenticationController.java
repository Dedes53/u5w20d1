package federicolepore.u5w19d4.controllers;

import federicolepore.u5w19d4.entities.Author;
import federicolepore.u5w19d4.exceptions.ValidationException;
import federicolepore.u5w19d4.payloads.AuthorDTO;
import federicolepore.u5w19d4.payloads.LoginDTO;
import federicolepore.u5w19d4.payloads.LoginRespDTO;
import federicolepore.u5w19d4.payloads.NewAuthorDTO;
import federicolepore.u5w19d4.services.AuthenticationService;
import federicolepore.u5w19d4.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthorService authorService;

    public AuthenticationController(AuthenticationService authenticationService, AuthorService authorService) {
        this.authenticationService = authenticationService;
        this.authorService = authorService;
    }


    // POST http://localhost:3001/auth
    @PostMapping("/login")
    public LoginRespDTO login(@RequestBody LoginDTO body) {
        return new LoginRespDTO(this.authenticationService.checkCredentialsAndGenerateToken(body));
    }

    // POST http://localhost:3001/auth
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED) //201
    public NewAuthorDTO saveUser(@RequestBody @Validated AuthorDTO body, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(e -> e.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }

        Author newA = this.authorService.save(body);
        return new NewAuthorDTO(newA.getId());
    }

}

























