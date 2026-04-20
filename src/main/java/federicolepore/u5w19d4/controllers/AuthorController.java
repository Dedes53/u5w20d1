package federicolepore.u5w19d4.controllers;


import federicolepore.u5w19d4.entities.Author;
import federicolepore.u5w19d4.exceptions.ValidationException;
import federicolepore.u5w19d4.payloads.AuthorDTO;
import federicolepore.u5w19d4.payloads.AuthorPayload;
import federicolepore.u5w19d4.payloads.NewAuthorDTO;
import federicolepore.u5w19d4.services.AuthorService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    // 1. POST http://localhost:3001/authors (+ req.body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewAuthorDTO save(@RequestBody @Validated AuthorDTO body, BindingResult validationResult) {

        if (validationResult.hasErrors()) {
            List<String> errors = validationResult.getFieldErrors().stream().map(error -> error.getDefaultMessage()).toList();
            throw new ValidationException(errors);
        }
        Author newAuthor = this.authorService.save(body);
        return new NewAuthorDTO((newAuthor.getId()));
    }


    // 2. GET http://localhost:3001/authors
    @GetMapping
    public Page<Author> getAuthors(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "nome") String sortBy) {

        return this.authorService.findAll(page, size, sortBy);
    }


    // 3. GET http://localhost:3001/authors/{authorId}
    @GetMapping("/{authorId}")
    public Author getById(@PathVariable UUID authorId) {
        return this.authorService.findById(authorId);
    }


    // 4. PUT http://localhost:3001/authors/{authorId} (+ req.body)
    @PutMapping("/{authorId}")
    public Author getByIdAndUpdate(@PathVariable UUID authorId, @RequestBody AuthorPayload body) {
        return this.authorService.findByIdAndUpdate(authorId, body);
    }


    // 5. DELETE http://localhost:3001/authors/{authorId}
    @DeleteMapping("/{authorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void getByIdAndDelete(@PathVariable UUID authorId) {
        this.authorService.findByIdAndDelete(authorId);
    }


    // 6 PATCH http://localhost:3001/authors/{authorId}/avatar
    @PatchMapping("/{authorId}/avatar")
    public Author uploadAvatar(@RequestParam("profile_picture") MultipartFile file, @PathVariable UUID authorId) {

        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getContentType());

        return this.authorService.avatarUpload(file, authorId);
    }
}
