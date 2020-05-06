package pl.most.typer.controller.typer.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.most.typer.exceptions.ResourceException;
import pl.most.typer.model.matches.Match;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.model.typer.TyperRound;
import pl.most.typer.model.typer.dto.TyperCompetitionDTO;
import pl.most.typer.model.typer.dto.TyperMatchDTO;
import pl.most.typer.service.footballservice.matches.MatchesService;
import pl.most.typer.service.typer.TyperCompetitionService;
import pl.most.typer.service.typer.TyperRoundService;

@Slf4j
@RestController
public class TyperControllerRest {

    private TyperCompetitionService typerCompetitionService;
    private MatchesService matchesService;
    private TyperRoundService typerRoundService;

    private final String ERROR_ATTR = "errorMessage";
    private final String ERROR_MSG = "Wystąpił błąd z obsługą: ";

    public TyperControllerRest(TyperCompetitionService typerCompetitionService, MatchesService matchesService, TyperRoundService typerRoundService) {
        this.typerCompetitionService = typerCompetitionService;
        this.matchesService = matchesService;
        this.typerRoundService = typerRoundService;
    }

    @PostMapping(value = "/typer/manager/rest/competitions",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> addNewMatchToTyperCompetition(@RequestBody TyperMatchDTO typerMatchDTO) {
        TyperCompetition typerCompetition;
        TyperRound typerRound;
        Match match;
        try {
            typerCompetition = typerCompetitionService.findById(typerMatchDTO.getTyperCompetitionId());
            typerRound = typerRoundService.findLatestTyperRoundByTyperCompetition(typerCompetition);
            match = matchesService.findByApiId(typerMatchDTO.getMatchId());
            typerRound.addTyperMatch(match);
            typerRoundService.update(typerRound);
        } catch (ResourceException ex) {
            log.info(ex.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(new TyperCompetitionDTO(typerCompetition.getName()));
    }

}
