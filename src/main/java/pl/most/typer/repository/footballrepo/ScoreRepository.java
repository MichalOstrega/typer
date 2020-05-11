package pl.most.typer.repository.footballrepo;

import org.springframework.data.repository.CrudRepository;
import pl.most.typer.model.matches.Score;

import java.util.Optional;

public interface ScoreRepository extends CrudRepository<Score, Integer> {

    Optional<Score> findById(Integer id);

    boolean existsById(Integer id);
}
