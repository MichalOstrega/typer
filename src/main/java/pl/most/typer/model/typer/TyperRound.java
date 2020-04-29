package pl.most.typer.model.typer;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import pl.most.typer.model.matches.Match;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TyperRound extends BaseModel {

    @ToString.Exclude
    @ManyToOne
    private TyperCompetition typerCompetition;

    private Integer number=0;
    
    @ToString.Exclude
    @OneToMany(mappedBy = "typerRound", cascade = CascadeType.ALL)
    private Set<TyperMatch> typerMatches;

    public TyperRound(TyperCompetition typerCompetition) {
        this.typerCompetition = typerCompetition;
        this.number = typerCompetition.getCurrentRound();
        this.typerMatches = new HashSet<>();
    }

    public void addTyperMatch(TyperMatch typerMatch){
        typerMatches.add(typerMatch);
        typerMatch.setTyperRound(this);
    }

    public void addTyperMatch(Match match){
        TyperMatch typerMatch = new TyperMatch(match);
        typerMatch.setTyperRound(this);
        typerMatches.add(typerMatch);
    }

    public void removeTyperMatch(TyperMatch typerMatch) {
        typerMatches.remove(typerMatch);
        typerMatch.setTyperRound(null);
    }
    
    
    
}
