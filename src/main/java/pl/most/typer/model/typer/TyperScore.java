package pl.most.typer.model.typer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.most.typer.model.matches.Score;
import pl.most.typer.model.matches.ScoreWinner;
import pl.most.typer.model.matches.TeamGoals;

import javax.persistence.*;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TyperScore extends BaseModel {

    @ManyToOne
    private TyperPlayer typerPlayer;

    @ManyToOne
    private TyperMatch typerMatch;

    @Enumerated(EnumType.STRING)
    private ScoreWinner scoreWinner;

    @OneToOne
    private TeamGoals teamGoals;

    public TyperScore(TyperPlayer typerPlayer, TyperMatch typerMatch, TeamGoals teamGoals) {

    }
}
