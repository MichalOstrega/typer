package pl.most.typer.service.typer;

import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.model.typer.TyperRound;

import java.util.List;

public interface TyperRoundService {
    List<TyperRound> findAllByTyperCompetitionId(Integer id);

    TyperRound findLatestTyperRoundByTyperCompetition(TyperCompetition typerCompetition) throws ResourceNotFoundException;

    TyperRound findRoundByTyperCompetition(TyperCompetition typerCompetition, Integer round) throws ResourceNotFoundException;

    void deleteAll(List<TyperRound> typerRounds);

    void saveAll(List<TyperRound> standings);

    void createNewTyperRound(TyperCompetition typerCompetition);

    TyperRound save(TyperRound typerRound) throws BadResourceException, ResourceAlreadyExistsException;

    TyperRound update(TyperRound typerRound);
}
