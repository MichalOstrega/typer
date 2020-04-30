package pl.most.typer.controller.typer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.most.typer.exceptions.ResourceException;
import pl.most.typer.model.account.PlayerDTO;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.model.typer.TyperRound;
import pl.most.typer.model.typer.dto.TyperCompetitionDTO;
import pl.most.typer.service.typer.TyperCompetitionService;
import pl.most.typer.service.typer.TyperPlayerService;
import pl.most.typer.service.typer.TyperRoundService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/typer/manager")
public class ManageTyperController {

    private final String ERROR_ATTR = "errorMessage";
    private final String ERROR_MSG = "Wystąpił błąd z obsługą: ";
    private TyperCompetitionService typerCompetitionService;
    private TyperPlayerService typerPlayerService;
    private TyperRoundService typerRoundService;

    private final int ROW_PER_PAGE = 5;

    public ManageTyperController(TyperCompetitionService typerCompetitionService, TyperPlayerService typerPlayerService, TyperRoundService typerRoundService) {
        this.typerCompetitionService = typerCompetitionService;
        this.typerPlayerService = typerPlayerService;
        this.typerRoundService = typerRoundService;
    }

    @ModelAttribute("typerCompetitionDTO")
    public TyperCompetitionDTO getTyperCompetitionDTO() {
        return new TyperCompetitionDTO();
    }

    @ModelAttribute
    public void getAll(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<TyperCompetition> typerCompetitions = typerCompetitionService.findAll(pageNumber, ROW_PER_PAGE);
        long count = typerCompetitionService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("typerCompetitions", typerCompetitions);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("prev", pageNumber - 1);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        model.addAttribute("typerCompetitionDTO", new TyperCompetitionDTO());
    }


    @GetMapping("/competitions")
    private String manageTyper(Model model) {
        return "typerTemplate/manageTyper";
    }

    @PostMapping("/competitions")
    private String addTyperCompetition(@Valid @ModelAttribute("typerCompetitionDTO") TyperCompetitionDTO typerCompetitionDTO,
                                       BindingResult bindingResult) {
        if (typerCompetitionDTO != null && !StringUtils.isEmpty(typerCompetitionDTO.getName())) {
            try {
                typerCompetitionService.save(typerCompetitionDTO.toTyperCompetition());
            } catch (ResourceException ex) {
                bindingResult.rejectValue(ex.getIssue(), "error." + ex.getIssue(), ex.getMessage());
            }
            if (bindingResult.hasErrors()) {
                return "typerTemplate/manageTyper";
            }
            return "redirect:/typer/manager/competitions";
        }
        return "typerTemplate/manageTyper";
    }

    @GetMapping(value = "competitions/{id}/delete")
    private String deleteTyperCompetition(@PathVariable("id") Integer id, Model model) {
        try {
            typerCompetitionService.deleteById(id);
        } catch (ResourceException e) {
            log.warn(e.getMessage());
            String error = ERROR_MSG + "usunięcie elementu";
            model.addAttribute(ERROR_ATTR, true);
            return "/typer/manager/competitions";
        }
        return "redirect:/typer/manager/competitions";
    }

    @GetMapping(value = "competitions/{id}/count")
    private String updateTyperCompetition(@PathVariable("id") Integer competitionId) {
        //TODO implementacja przeliczenia punktów z kolejki
        typerCompetitionService.countRound(competitionId);
        return "redirect:/typer/manager/competitions";
    }

    @GetMapping(value = "competitions/{id}/edit")
    private String editTyperCompetition(@PathVariable("id") Integer id,
                                        Model model) {
        TyperCompetition typerCompetition = null;
        TyperRound typerRound = null;
        try {
            typerCompetition = typerCompetitionService.findById(id);
            typerRound = typerRoundService.findLatestTyperRoundByTyperCompetition(typerCompetition);
        } catch (ResourceException ex) {
            log.warn(ex.getMessage());
            model.addAttribute(ERROR_ATTR, ERROR_MSG + "nie znaleziono " + ex.getResource());
            return "typerTemplate/manageTyper";
        }
        List<PlayerDTO> playerDTOS = typerPlayerService.findAll();
        model.addAttribute("typerCompetition", typerCompetition);
        model.addAttribute("players", playerDTOS);
        model.addAttribute("playerDTO", new PlayerDTO());
        model.addAttribute("typerRound", typerRound);
        return "typerTemplate/typerCompetitionEdit";
    }


    @PostMapping(value = "competitions/{id}/edit")
    private String updateTyperCompetition(@PathVariable("id") Integer id,
                                          @Valid @ModelAttribute("typerCompetition") TyperCompetition typerCompetition,
                                          BindingResult bindingResult,
                                          Model model) {
        try {
            typerCompetition.setId(id);
            typerCompetitionService.update(typerCompetition);
            return "redirect:/typer/manager/competitions/" + id + "/edit";
        } catch (ResourceException ex) {
            log.warn(ex.getMessage());
            model.addAttribute(ERROR_ATTR, ERROR_MSG + "edycji " + ex.getResource());
            bindingResult.rejectValue(ex.getIssue(), "error." + ex.getIssue(), ex.getMessage());
            return "typerTemplate/typerCompetitionEdit";
        }
    }

    @GetMapping(value = "competitions/{id}/players/{playerId}/delete")
    private String deleteTyperCompetitionPlayer(@PathVariable("id") Integer competitionId,
                                                @PathVariable("playerId") Integer playerId,
                                                Model model) {
        try {
            typerCompetitionService.deletePlayerFromCompetition(competitionId, playerId);
        } catch (ResourceException ex) {
            log.warn(ex.getMessage());
            String error = ERROR_MSG + "usunięcie elementu";
            model.addAttribute(ERROR_ATTR, true);
            return "/typer/manager/competitions/" + competitionId + "/edit";
        }
        return "redirect:/typer/manager/competitions/" + competitionId + "/edit";
    }
    @PostMapping("/competitions/{id}/players/add")
    private String addPlayerToCompetition(@Valid @ModelAttribute("userDTO") PlayerDTO playerDTO,
                                          @PathVariable("id") Integer competitionId,
                                            BindingResult bindingResult,
                                          Model model) {
        if (playerDTO != null) {
            try {
                typerCompetitionService.addPlayerToCompetition(competitionId, playerDTO.getId());


            } catch (ResourceException ex) {
                log.warn(ex.getMessage());
                model.addAttribute(ERROR_ATTR, ERROR_MSG + "dodania " + ex.getResource());
                bindingResult.rejectValue(ex.getIssue(), "error." + ex.getIssue(), ex.getMessage());
                return "/typer/manager/competitions/" + competitionId + "/edit";
            }
            return "redirect:/typer/manager/competitions/" + competitionId + "/edit";
        }
        return "typerTemplate/typerCompetitionEdit";
    }

}
