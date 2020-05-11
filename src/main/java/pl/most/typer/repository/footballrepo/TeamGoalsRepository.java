package pl.most.typer.repository.footballrepo;


import org.springframework.data.repository.CrudRepository;
import pl.most.typer.model.matches.Score;
import pl.most.typer.model.matches.TeamGoals;

import java.util.Optional;

public interface TeamGoalsRepository extends CrudRepository<TeamGoals, Integer> {
    Optional<TeamGoals> findById(Integer id);

    boolean existsById(Integer apiId);


}
