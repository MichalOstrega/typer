package pl.most.typer.model.typer;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Data
@Entity
public class TyperRound extends BaseModel {

    @ToString.Exclude
    @ManyToOne
    private TyperCompetition typerCompetition;

    @ColumnDefault("0")
    private Integer number;
}
