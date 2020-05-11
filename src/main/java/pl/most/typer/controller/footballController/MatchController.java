package pl.most.typer.controller.footballController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.most.typer.exceptions.ResourceException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.matches.Match;
import pl.most.typer.service.footballservice.competition.CompetitionService;
import pl.most.typer.service.footballservice.matches.MatchesService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(value = "/competitions")
public class MatchController {

    private final CompetitionService competitionService;
    private final MatchesService matchesService;

    public MatchController(CompetitionService competitionService, MatchesService matchesService) {
        this.competitionService = competitionService;
        this.matchesService = matchesService;
    }

    @GetMapping(value = "/{id}/matches/update")
    public String updateMatchInfo(@PathVariable(value = "id") Integer id) {
        Optional<Match> optionalMatch = matchesService.findFirstByCompetition(new Competition(id, null));
        if (optionalMatch.isPresent()) {
            return "redirect:/competitions/" + id + "/matches";
        }
        HttpStatus status = matchesService.getMatchInfoFromExternalApi(id);
        if (status.is2xxSuccessful()) {
            return "redirect:/competitions/" + id + "/matches";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping(value = "/{id}/matches")
    private String getMatchesByStages(@PathVariable("id") Integer id,
                                      @RequestParam(name = "stage", required = false) String stage,
                                      @RequestParam(name = "TCID", required = false) String typerCompetitionId,
                                      Model model) throws ResourceException {
        Optional<Competition> optionalCompetition = Optional.of(competitionService.getCompetition(id));
        Optional<Match> optionalMatch = matchesService.findFirstByCompetition(new Competition(id, null));

        if (checkIfCompetitionMatchExist(optionalCompetition, optionalMatch)) {
            return "redirect:/competitions/" + id + "/matches/update";
        }

        List<Match> matchByCompetition = null;

        if (stage != null) {
            try {
                matchByCompetition = matchesService
                        .findAllByCompetitionAndStage(optionalCompetition.get(), stage);
            } catch (ResourceException ex) {
                log.warn(ex.getMessage());
                throw ex;
            }
        } else {
            try {
                List<String> stages = matchesService.getStages(optionalCompetition.get());
                if (stages.size() == 1) {
                    return "redirect:/competitions/" + id + "/matches?stage=" + stages.stream().findFirst().get();
                }

                model.addAttribute("apiId", id);
                model.addAttribute("competitionName", competitionService.getCompetitionName(id));
                model.addAttribute("stages", stages);

                return "stages";
            } catch (ResourceException e) {
                log.warn(e.getMessage());
                throw e;
            }

        }

        if (matchByCompetition != null) {
            try {
                LinkedHashMap<String, List<Match>> matchesByGroup = getGroupLinkedHashMap(matchByCompetition);

                model.addAttribute("competitionName", competitionService.getCompetitionName(id));
                model.addAttribute("matchesByGroup", matchesByGroup);
                model.addAttribute("stageName", stage);
                model.addAttribute("TCID", typerCompetitionId);
            } catch (ResourceException e) {
                log.warn(e.getMessage());
                throw e;
            }
        }
        return "matches";
    }

    private LinkedHashMap<String, List<Match>> getStageLinkedHashMap(List<Match> matchByCompetition) {
        return matchByCompetition
                .stream()
                .collect(Collectors.groupingBy(Match::getStage, LinkedHashMap::new, Collectors.toList()));
    }

    private LinkedHashMap<String, List<Match>> getGroupLinkedHashMap(List<Match> matchByCompetition) {
        return matchByCompetition
                .stream()
                .collect(Collectors.groupingBy(match -> match.getGroup() == null ? "absentGroup" : match.getGroup(), LinkedHashMap::new, Collectors.toList()));
    }

    private boolean checkIfCompetitionMatchExist(Optional<Competition> optionalCompetition, Optional<Match> optionalMatch) {
        if (optionalCompetition.isEmpty() || optionalMatch.isEmpty()) {
            return true;
        }
        return false;
    }

}
