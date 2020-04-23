package pl.most.typer.service.typer;

import pl.most.typer.model.competition.LeagueStanding;
import pl.most.typer.model.typer.TyperLeagueStanding;

import java.util.List;

public interface TyperLeagueStandingService {
    

    List<TyperLeagueStanding> findByTyperCompetitionId(Integer id);


    void deleteAllByPlayerAndCompetition(Integer playerId, Integer competitionId);
}
