package pl.most.typer.service.footballservice.matches;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.model.competition.Season;
import pl.most.typer.model.matches.Score;
import pl.most.typer.repository.footballrepo.ScoreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ScoreServiceDefault implements ScoreService {

    private final ScoreRepository scoreRepository;

    public ScoreServiceDefault(ScoreRepository scoreRepository) {
        this.scoreRepository = scoreRepository;
    }

    @Override
    public void saveAll(List<Score> scoreFromMatchDTO) throws ResourceAlreadyExistsException, BadResourceException {
        for (Score score : scoreFromMatchDTO) {
            if (StringUtils.isEmpty(score)) {
                if (score.getId() != null && existsById(score.getId())) {
                    ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("Score with id: " +
                            score.getId() + " already exists");
                    ex.setResource("Score");
                    ex.setIssue("id");
                } else {
                    scoreRepository.save(score);
                }
            } else {
                BadResourceException ex = new BadResourceException("Failed to save Score");
                ex.setErrorMessage("Score is null or empty");
            }
        }
    }

    @Override
    public boolean existsById(Integer id) {
        return scoreRepository.existsById(id);
    }
}
