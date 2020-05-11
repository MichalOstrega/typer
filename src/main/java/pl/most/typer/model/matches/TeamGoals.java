package pl.most.typer.model.matches;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import pl.most.typer.model.competition.Competition;

import javax.persistence.*;
import java.util.Objects;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamGoals {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer homeTeam;
    private Integer awayTeam;

//    //TODO Przedyskutować czy to jest tu potrzebne? Mozna usunąć, póki co zakomentowałem wszystkie usage, gdyby jednak coś było nie tak, potem sie usunie
//    @OneToOne
//    @ToString.Exclude
//    private Score score;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamGoals teamGoals = (TeamGoals) o;
        return Objects.equals(id, teamGoals.id) &&
                Objects.equals(homeTeam, teamGoals.homeTeam) &&
                Objects.equals(awayTeam, teamGoals.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, homeTeam, awayTeam);
    }
}
