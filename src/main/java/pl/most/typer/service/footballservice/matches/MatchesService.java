package pl.most.typer.service.footballservice.matches;

import org.springframework.http.HttpStatus;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.matches.Match;

import java.util.List;
import java.util.Optional;

public interface MatchesService {

    Match findByApiId(Integer apiId);

    HttpStatus getMatchInfoFromExternalApi(Integer competitionId);

    void saveAll(List<Match> matches);

    List<Match> findAllByCompetition(Competition competition);

    List<Match> findAllByCompetitionAndStage(Competition competition, String stage);

    Optional<Match> findFirstByCompetition(Competition competiton);

    boolean existsByApiId(Integer apiId);

    List<String> getStages(Competition competition);
}
