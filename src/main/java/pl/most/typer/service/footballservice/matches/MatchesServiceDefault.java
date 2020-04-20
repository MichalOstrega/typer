package pl.most.typer.service.footballservice.matches;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.competition.Season;
import pl.most.typer.model.competition.Team;
import pl.most.typer.model.matches.Match;
import pl.most.typer.model.matches.MatchDTO;
import pl.most.typer.model.matches.Score;
import pl.most.typer.model.matches.TeamGoals;
import pl.most.typer.repository.footballrepo.MatchesRepository;
import pl.most.typer.service.footballservice.FootballApiService;
import pl.most.typer.service.footballservice.competition.CompetitionService;
import pl.most.typer.service.footballservice.competition.SeasonService;
import pl.most.typer.service.footballservice.competition.TeamService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MatchesServiceDefault implements MatchesService {

    private final CompetitionService competitionService;
    private final TeamService teamService;
    private final SeasonService seasonService;
    private final ScoreService scoreService;
    private final TeamGoalsService teamGoalsService;
    private final MatchesRepository matchesRepository;
    private final FootballApiService footballApiService;


    public MatchesServiceDefault(CompetitionService competitionService, TeamService teamService, SeasonService seasonService, ScoreService scoreService, TeamGoalsService teamGoalsService, MatchesRepository matchesRepository, FootballApiService footballApiService) {
        this.competitionService = competitionService;
        this.teamService = teamService;
        this.seasonService = seasonService;
        this.scoreService = scoreService;
        this.teamGoalsService = teamGoalsService;
        this.matchesRepository = matchesRepository;
        this.footballApiService = footballApiService;
    }

    @Override
    public HttpStatus getMatchInfoFromExternalApi(Integer competitionId) {
        List<String> endpoint = Arrays.asList("competitions", competitionId.toString(), "matches");
        Map<String, String> filters = new HashMap<>();

        ResponseEntity<MatchDTO> responseEntity = footballApiService
                .getExternalData(endpoint, filters, MatchDTO.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            MatchDTO matchDTO = responseEntity.getBody();
            if (matchDTO != null) {
                Competition competition = competitionService.save(matchDTO.getCompetition());
                setCompetitionForMatches(matchDTO, competition);
                setCompetitionForSeasons(matchDTO, competition);
                setMatchForScore(matchDTO);
                setScoreForTeamGoals(matchDTO);

                seasonService.saveAll(getSeasonsFromMatchDTO(matchDTO));
                teamService.saveAll(getTeamsFromMatchDTO(matchDTO));

                saveAll(matchDTO.getMatches());

                return HttpStatus.CREATED;
            } else {
                return HttpStatus.BAD_GATEWAY;
            }
        }
        return HttpStatus.BAD_GATEWAY;
    }

    @Override
    public void saveAll(List<Match> matches) {
        for (Match match : matches) {
            Optional<Match> byApiId = matchesRepository.findByApiId(match.getApiId());
            byApiId.orElseGet(() -> matchesRepository.save(match));
        }
    }

    @Override
    public List<Match> findAllByCompetition(Competition competition) {
        return matchesRepository.findAllByCompetitionOrderByUtcDateDesc(competition);
    }

    @Override
    public Optional<Match> findFirstByCompetition(Competition competiton) {
        return matchesRepository.findFirstByCompetition(competiton);
    }


    private void setCompetitionForMatches(MatchDTO matchDTO, Competition competition) {
        matchDTO.getMatches().forEach(match -> match.setCompetition(competition));
    }

    private void setCompetitionForSeasons(MatchDTO matchDTO, Competition competition) {
        getSeasonsFromMatchDTO(matchDTO).forEach(season -> season.setCompetition(competition));
    }

    private void setMatchForScore(MatchDTO matchDTO) {
        matchDTO.getMatches().forEach(match -> match.getScore().setMatch(match));
    }

    private void setScoreForTeamGoals(MatchDTO matchDTO) {
        matchDTO.getMatches().forEach(match -> {
            match.getScore().getPenalties().setScore(match.getScore());
            match.getScore().getHalfTime().setScore(match.getScore());
            match.getScore().getFullTime().setScore(match.getScore());
            match.getScore().getExtraTime().setScore(match.getScore());
        });
    }

    //zeby dzialalo distinct potrzebne jest nadpisanie metody equals w klasie season
    private List<Season> getSeasonsFromMatchDTO(MatchDTO matchDTO) {
        return matchDTO.getMatches()
                .stream()
                .map(Match::getSeason)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Team> getTeamsFromMatchDTO(MatchDTO matchDTO) {
        return matchDTO.getMatches().stream()
                .flatMap(match -> Stream.of(match.getAwayTeam(), match.getHomeTeam()))
                .collect(Collectors.toList());
    }

    private List<Score> getScoreFromMatchDTO(MatchDTO matchDTO) {
        return matchDTO.getMatches()
                .stream()
                .map(Match::getScore)
                .collect(Collectors.toList());
    }

    private List<TeamGoals> getTeamGoalsFromMatchDTO(MatchDTO matchDTO) {
        return matchDTO.getMatches()
                .stream()
                .map(Match::getScore)
                .flatMap(score -> Stream.of(score.getExtraTime(), score.getFullTime()
                        , score.getHalfTime(), score.getPenalties()))
                .collect(Collectors.toList());
    }

}
