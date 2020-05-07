package pl.most.typer.model.typer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.most.typer.model.typer.dto.TyperCompetitionDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class TyperCompetition extends BaseModel {



    @ToString.Exclude
    @ManyToMany
    private Collection<TyperPlayer> typerPlayers;

    @Column(unique = true)
    private String name;



    private Integer currentRound;

    private LocalDateTime lastUpdated;

    public TyperCompetition(String name) {
        this.name = name;
        this.lastUpdated = LocalDateTime.now();
        this.typerPlayers = new ArrayList<>();
        this.currentRound=-1;
    }

    public TyperCompetition(TyperCompetitionDTO typerCompetitionDTO) {
        this(typerCompetitionDTO.getName());
    }


    public void addTyperPlayer(TyperPlayer typerPlayer) {
        if(!isParticipating(typerPlayer)) {
            this.typerPlayers.add(typerPlayer);
            if (!typerPlayer.isAssignedToTyperCompetition(this)) {
                typerPlayer.addTyperCompetition(this);
            }
        }
    }

    public void removeTyperPlayer(TyperPlayer typerPlayer) {
        this.typerPlayers.remove(typerPlayer);
        if (typerPlayer.isAssignedToTyperCompetition(this)) {
            typerPlayer.removeTyperCompetition(this);
        }

    }

    public boolean isParticipating(TyperPlayer typerPlayer) {
        return this.typerPlayers.contains(typerPlayer);
    }

    public Integer incrementedCurrentRound() {
        this.currentRound += 1;
        return currentRound;
    }
}
