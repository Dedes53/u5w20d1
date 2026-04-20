package federicolepore.u5w19d4.repositories;

import federicolepore.u5w19d4.entities.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {
    boolean existsByTitolo(String titolo);
}
