package pl.most.typer.model.typer.dto;

import lombok.Data;
import pl.most.typer.model.typer.TyperMatch;

import javax.validation.constraints.NotNull;

@Data
public class TyperMatchDTO {

    @NotNull
    private Integer typerCompetitionId;

    @NotNull
    private Integer matchId;


}
