package pl.most.typer.model.matches;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;


@Data
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ScoreWinner winner;

    @Enumerated(EnumType.STRING)
    private ScoreDuration duration;

    @OneToOne(cascade = CascadeType.ALL)
    private TeamGoals fullTime;
    @OneToOne(cascade = CascadeType.ALL)
    private TeamGoals halfTime;
    @OneToOne(cascade = CascadeType.ALL)
    private TeamGoals extraTime;
    @OneToOne(cascade = CascadeType.ALL)
    private TeamGoals penalties;

    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Match match;


}
