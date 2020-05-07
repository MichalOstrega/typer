package pl.most.typer.controller.footballController.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.most.typer.exceptions.ResourceException;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.competition.Standing;
import pl.most.typer.model.matches.Match;
import pl.most.typer.service.footballservice.competition.CompetitionService;
import pl.most.typer.service.footballservice.matches.MatchesService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/rest/competitions")
public class MatchControllerRest {

    private CompetitionService competitionService;
    private MatchesService matchesService;
    private final String ERROR_ATTR = "errorMessage";
    private final String ERROR_MSG = "Wystąpił błąd z obsługą: ";

    public MatchControllerRest(CompetitionService competitionService, MatchesService matchesService) {
        this.competitionService = competitionService;
        this.matchesService = matchesService;
    }

    @GetMapping(value = "/{id}/matches", produces = "application/json")
    private ResponseEntity<List<String>> getStandingStage(@PathVariable("id") Integer competitionID) {
        Competition competition = null;
        try {
            competition = competitionService.findByApiId(competitionID);

        } catch (ResourceException ex) {
            log.info(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(matchesService.getStages(competition));
    }
}
