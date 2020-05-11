package pl.most.typer.repository.footballrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.matches.Match;

import java.util.List;
import java.util.Optional;

public interface MatchesRepository extends JpaRepository<Match, Integer> {

    Optional<Match> findByApiId(Integer id);

    List<Match> findAllByCompetitionOrderByUtcDateDesc(Competition competitionId);

    Optional<Match> findFirstByCompetition(Competition competiton);

    List<Match> findAllByCompetitionAndStageOrderByUtcDateDesc(Competition competition, String stage);

    boolean existsByApiId(Integer apiId);
}

