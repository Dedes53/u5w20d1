package federicolepore.u5w19d4.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import federicolepore.u5w19d4.entities.Author;
import federicolepore.u5w19d4.exceptions.BadRequestException;
import federicolepore.u5w19d4.exceptions.NotFoundException;
import federicolepore.u5w19d4.payloads.AuthorDTO;
import federicolepore.u5w19d4.payloads.AuthorPayload;
import federicolepore.u5w19d4.repositories.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class AuthorService {

    private final AuthorRepository aRepo;
    private final Cloudinary cloudinaryUploader;

    public AuthorService(AuthorRepository aRepo, Cloudinary cloudinaryUploader) {
        this.aRepo = aRepo;
        this.cloudinaryUploader = cloudinaryUploader;
    }


    //1
    public Author save(AuthorDTO body) {
        if (this.aRepo.existsByEmail(body.email()))
            throw new BadRequestException("L'indirizzo email " + body.email() + "è già in uso");
        Author a = new Author(body.nome(), body.cognome(), body.email(), body.dataDiNascita());
        this.aRepo.save(a);
        return a;
    }


    // 2
    public Page<Author> findAll(int page, int size, String sortBy) {

        if (size > 100) size = 10;
        if (size < 0) size = 0;
        if (page < 0) page = 0;
        // imposto la pagina per determinare quanti risultati ritornare al frontend e comme ordinarli
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.aRepo.findAll(pageable);
    }


    //3
    public Author findById(UUID id) {
        return this.aRepo.findById(id).orElseThrow(() -> new NotFoundException(id));
    }


    //4
    public Author findByIdAndUpdate(UUID id, AuthorPayload body) {

        Author found = this.findById(id);
        // verifico che la mail non sia doppia con un altro autore già in uso
        if (Objects.equals(found.getEmail(), body.getEmail())) {
            if (this.aRepo.existsByEmail(body.getEmail()))
                throw new BadRequestException("L'indirizzo email " + body.getEmail() + "è già in uso");
        }
        // modifico l'utente trovato
        found.setNome(body.getNome());
        found.setCognome(body.getCognome());
        found.setEmail(body.getEmail());
        found.setDataDiNascita(body.getDataDiNascita());
        found.setAvatarUrl("https://ui-avatars.com/api?name=" + body.getNome() + "+" + body.getCognome());

        Author aUpdated = this.aRepo.save(found);
        log.info("L'utente con id: " + aUpdated.getId() + "è stato salvato correttamente");
        return aUpdated;
    }


    //5
    public void findByIdAndDelete(UUID id) {
        Author found = this.findById(id);
        this.aRepo.delete(found);
    }


    // 6
    public Author avatarUpload(MultipartFile file, UUID authorId) {

        // 1 - eventuali controlli sui file: dimensioni, tipo ecc
        // 2 - trovo l'autore a sui cui effettuare l'upload
        Author toUpload = this.findById(authorId);

        //3 - effettivo upload del file su Cloudinary
        String url;
        try {
            Map result = cloudinaryUploader.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            url = (String) result.get("secure_url");
            System.out.println(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //4 - settare l'url all'avatar e salvare la modifica
        toUpload.setAvatarUrl(url);
        aRepo.save(toUpload);

        // 5 - ritorno il profilo modificato
        return toUpload;

    }


}

