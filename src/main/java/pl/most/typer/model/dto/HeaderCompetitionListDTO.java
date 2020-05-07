package pl.most.typer.model.dto;

import lombok.Data;
import org.springframework.stereotype.Component;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.service.footballservice.competition.CompetitionService;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
public class HeaderCompetitionListDTO {

    private CompetitionService competitionService;
    private List<Competition> competitions = new ArrayList<>();

    public HeaderCompetitionListDTO(CompetitionService competitionService) {
        this.competitionService=competitionService;
        //Temporary
        this.competitions.add(new Competition(2013,"Série A"));
        this.competitions.add(new Competition(2016,"Championship"));
        this.competitions.add(new Competition(2021,"Premier League"));
        this.competitions.add(new Competition(2001,"Uefa Champions League"));
        this.competitions.add(new Competition(2018,"European Championship"));
//        this.competitions.add(new Competition(2015,"Ligue 1"));
//        this.competitions.add(new Competition(2002,"Bundesliga"));
//        this.competitions.add(new Competition(2019,"Serie A"));
//        this.competitions.add(new Competition(2003,"Eredivisie"));
//        this.competitions.add(new Competition(2017,"Primeira Liga"));
//        this.competitions.add(new Competition(2014,"Primera Division"));
//        this.competitions.add(new Competition(2000,"Fifa World Cup"));

        //Mysql
        //this.competitions= competitionService.getAll();
    }
}
