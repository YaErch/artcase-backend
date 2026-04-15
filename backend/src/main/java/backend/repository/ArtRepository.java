package backend.repository;

import  backend.model.Art;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtRepository extends JpaRepository<Art, Long> {

    List<Art> findByTagsContaining(String tag);
}