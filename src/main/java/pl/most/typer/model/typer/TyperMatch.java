package pl.most.typer.model.typer;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.engine.spi.CascadingAction;
import pl.most.typer.model.matches.Match;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TyperMatch extends BaseModel {

    @ManyToOne
    private TyperRound typerRound;

    @ManyToOne
    private Match match;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typerMatch")
    private Set<TyperScore> typerScores;

    public TyperMatch(Match match) {
        this.match = match;
        typerScores = new HashSet<>();
    }

    public void addTyperScore(TyperScore typerScore){
        typerScores.add(typerScore);
        typerScore.setTyperMatch(this);
    }
    

    public void removeTyperScore(TyperScore typerScore) {
        typerScores.remove(typerScore);
        typerScore.setTyperMatch(null);
    }
}
