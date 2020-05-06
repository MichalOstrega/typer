package pl.most.typer.model.typer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.most.typer.model.matches.ScoreWinner;
import pl.most.typer.model.matches.TeamGoals;

import javax.persistence.*;
import java.util.Objects;

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

    @OneToOne
    private TyperPoints typerPoints;

    public TyperScore(TyperPlayer typerPlayer, TyperMatch typerMatch, TeamGoals teamGoals) {
        this.typerPlayer = typerPlayer;
        this.typerMatch = typerMatch;
        this.teamGoals = teamGoals;
        this.scoreWinner = ScoreWinner.getScoreWinner(teamGoals);
        this.typerPoints = new TyperPoints(this);
    }

    public TyperPoints calculatePoints() {
        //TODO
        return this.typerPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TyperScore that = (TyperScore) o;
        return Objects.equals(typerPlayer, that.typerPlayer) &&
                Objects.equals(typerMatch, that.typerMatch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), typerPlayer, typerMatch);
    }
}
