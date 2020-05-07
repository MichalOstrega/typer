package pl.most.typer.model.typer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.most.typer.model.matches.ScoreWinner;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@EqualsAndHashCode(callSuper = true)
public class TyperPoints extends BaseModel {

    private Integer points;

    private boolean calculated;

    @OneToOne
    private TyperScore typerScore;

    protected TyperPoints(TyperScore typerScore) {
        this.typerScore = typerScore;
        this.points = 0;
        this.calculated = false;
    }


}
