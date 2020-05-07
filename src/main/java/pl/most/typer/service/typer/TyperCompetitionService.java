package pl.most.typer.service.typer;

import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.model.typer.TyperCompetition;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TyperCompetitionService {

    TyperCompetition save(TyperCompetition typerCompetition) throws BadResourceException, ResourceAlreadyExistsException;

    TyperCompetition findById(Integer id) throws ResourceNotFoundException;

    List<TyperCompetition> findAll(int pageNumber, int rowPerPage);
    List<TyperCompetition> findAll();

    void update(TyperCompetition typerCompetition);

    void deleteById(Integer id) throws ResourceNotFoundException;

    Long count();

    void deletePlayerFromCompetition(Integer competitionId, Integer playerId);

    void addPlayerToCompetition(Integer competitionId, Integer id);

    void countRound(Integer competitionId);
}
