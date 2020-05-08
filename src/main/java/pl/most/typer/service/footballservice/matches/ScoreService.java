package pl.most.typer.service.footballservice.matches;

import pl.most.typer.model.matches.Score;

import java.util.List;

public interface ScoreService {
    void saveAll(List<Score> scoreFromMatchDTO);

    boolean existsById(Integer id);
}
