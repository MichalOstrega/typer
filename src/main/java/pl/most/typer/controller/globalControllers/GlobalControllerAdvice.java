package pl.most.typer.controller.globalControllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.most.typer.model.dto.HeaderCompetitionListDTO;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.repository.typerrepo.TyperCompetitionRepository;
import pl.most.typer.service.typer.TyperCompetitionService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Data
@ControllerAdvice
public class GlobalControllerAdvice {

    public static final String DEFAULT_ERROR_VIEW = "error";
    private HeaderCompetitionListDTO headerCompetitionListDTO;
    private TyperCompetitionService typerCompetitionService;

    public GlobalControllerAdvice(
            HeaderCompetitionListDTO headerCompetitionListDTO,
            TyperCompetitionService typerCompetitionService) {
        this.headerCompetitionListDTO = headerCompetitionListDTO;
        this.typerCompetitionService = typerCompetitionService;
    }

    @ModelAttribute("navCompetitions")
    public List<Competition> getListOfAvailableCompetitions() {
        return headerCompetitionListDTO.getCompetitions();
    }

    @ModelAttribute("navTyperCompetitions")
    public List<TyperCompetition> getTypersCompetitions() {
        return typerCompetitionService.findAll();
    }


    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ModelAndView handleException(HttpServletRequest req, Exception ex) {
        ModelAndView mav = new ModelAndView();
        log.error(ex.getMessage(),ex);
        mav.addObject("msg", ex.getMessage());
        mav.addObject("url", req.getRequestURL());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }


    @GetMapping(value = {"/error"})
    public String ErrorPage() {
        return "error";
    }

}
