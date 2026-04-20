package federicolepore.u5w19d4.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import federicolepore.u5w19d4.entities.BlogPost;
import federicolepore.u5w19d4.exceptions.BadRequestException;
import federicolepore.u5w19d4.exceptions.NotFoundException;
import federicolepore.u5w19d4.payloads.BlogPostPayload;
import federicolepore.u5w19d4.repositories.BlogPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class BlogPostService {

    private final BlogPostRepository bpRepo;
    private final Cloudinary cloudinaryUploader;

    public BlogPostService(BlogPostRepository bpRepo, Cloudinary cloudinaryUploader) {
        this.bpRepo = bpRepo;
        this.cloudinaryUploader = cloudinaryUploader;
    }


    // METODI DA RICHIAMARE NEL CONTROLLER PER CRUD
    //1
    public BlogPost save(BlogPostPayload body) {
        if (this.bpRepo.existsByTitolo(body.getTitolo()))
            throw new BadRequestException("Esiste già un post con il titolo: " + body.getTitolo());
        BlogPost b = new BlogPost(body.getTitolo(), body.getContenuto(), body.getTempoDiLettura(), body.getAuthorId());
        this.bpRepo.save(b);
        return b;
    }


    //2
    public Page<BlogPost> findALl(int page, int size, String sortBy) {
        if (size > 100) size = 10;
        if (size < 0) size = 0;
        if (page < 0) page = 0;
        // imposto la pagina per determinare quanti risultati ritornare al frontend e comme ordinarli
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.bpRepo.findAll(pageable);
    }


    //3
    public BlogPost findById(UUID id) {
        return this.bpRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    // 4
    public BlogPost findByIdAndUpdate(UUID id, BlogPostPayload body) {

        BlogPost found = this.findById(id);
        if (!found.getTitolo().equals(body.getTitolo())) {
            if (this.bpRepo.existsByTitolo(body.getTitolo()))
                throw new BadRequestException("Esiste già un post con il titolo: " + body.getTitolo());
        }

        found.setTitolo(body.getTitolo());
        found.setContenuto(body.getContenuto());
        found.setTempoDiLettura(body.getTempoDiLettura());
        found.setAuthorId(body.getAuthorId());
        found.setCoverUrl("https://picsum.photos/200/300");

        this.bpRepo.save(found);
        log.info("Il post " + found.getTitolo() + "è stato salvato correttamente");
        return found;
    }

    // 5
    public void findByIdAndDelete(UUID id) {
        BlogPost found = this.findById(id);
        this.bpRepo.delete(found);
    }


    // 6
    public BlogPost coverUpload(MultipartFile file, UUID blogPostId) {
        BlogPost toUpload = this.findById(blogPostId);
        String url;
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            url = (String) result.get("secure_url");
            System.out.println(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        toUpload.setCoverUrl(url);
        bpRepo.save(toUpload);

        return toUpload;
    }
}














