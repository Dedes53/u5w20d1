package federicolepore.u5w19d4.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "blog_post")
public class BlogPost {

    @Id
    @GeneratedValue
    private UUID id;
    private String titolo;
    private String coverUrl;
    private String contenuto;
    private int tempoDiLettura;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author authorId;

    public BlogPost(String titolo, String contenuto, int tempoDiLettura, Author authorId) {
        this.id = UUID.randomUUID();
        this.titolo = titolo;
        this.coverUrl = "https://picsum.photos/200/300";
        this.contenuto = contenuto;
        this.tempoDiLettura = tempoDiLettura;
        this.authorId = authorId;
    }


}
