package federicolepore.u5w19d4.controllers;


import federicolepore.u5w19d4.entities.BlogPost;
import federicolepore.u5w19d4.payloads.BlogPostPayload;
import federicolepore.u5w19d4.services.BlogPostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


/*
    1- GET http://localhost:port/blogPosts -> ritorna array di blogPosts
    2- POST http://localhost:port/blogPosts (+payload) -> salva un nuovo blogPost
    3- GET http://localhost:port/blogPosts/{blogPostID} -> ritorna uno specifico blogPost
    4- PUT http://localhost:port/blogPosts/{blogPostID} (+payload) -> aggiorna il blogPost
    5- DELETE http://localhost:port/blogPosts/{blogPostID} -> elimina il blogPost
*/


@RestController
@RequestMapping("/blogPosts")
public class BlogPostController {
    // prima gli passo il service e poi creo il costruttore

    private BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    // 1 POST http://localhost:port/blogPosts (+payload) -> salva un nuovo blogPost
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BlogPost save(@RequestBody BlogPostPayload body) {
        return this.blogPostService.save(body);
    }


    // 2 GET http://localhost:port/blogPosts -> ritorna array di blogPosts
    @GetMapping
    public Page<BlogPost> getBlogPosts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "titolo") String sortBy) {

        return this.blogPostService.findALl(page, size, sortBy);
    }


    // 3 GET http://localhost:port/blogPosts/{blogPostID} -> ritorna uno specifico blogPost
    @GetMapping("/{blodPostId}")
    public BlogPost getById(@PathVariable UUID blodPostId) {
        return this.blogPostService.findById(blodPostId);
    }

    // 4 PUT http://localhost:port/blogPosts/{blogPostID} (+payload) -> aggiorna il blogPost
    @PutMapping("/{blodPostId}")
    public BlogPost findByIdAndUpdate(@PathVariable UUID blodPostId, @RequestBody BlogPostPayload body) {
        return this.blogPostService.findByIdAndUpdate(blodPostId, body);
    }

    // 5
    @DeleteMapping("/{blodPostId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable UUID blodPostId) {
        this.blogPostService.findByIdAndDelete(blodPostId);
    }


    // 6 PATCH http://localhost:3001/blogPosts/{blogPostID}/cover
    @PatchMapping("/{blodPostId}/cover")
    public BlogPost upLoadCover(@RequestParam("cover") MultipartFile file, @PathVariable UUID blodPostId) {
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getSize());
        System.out.println(file.getContentType());

        return this.blogPostService.coverUpload(file, blodPostId);
    }

}




















