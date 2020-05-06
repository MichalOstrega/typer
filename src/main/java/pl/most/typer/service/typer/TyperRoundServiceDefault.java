package pl.most.typer.service.typer;

import org.springframework.stereotype.Service;
import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.model.typer.TyperRound;
import pl.most.typer.repository.typerrepo.TyperRoundRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TyperRoundServiceDefault implements TyperRoundService {

    private TyperRoundRepository typerRoundRepository;

    public TyperRoundServiceDefault(TyperRoundRepository typerRoundRepository) {
        this.typerRoundRepository = typerRoundRepository;
    }

    @Override
    public List<TyperRound> findAllByTyperCompetitionId(Integer id) {
        TyperCompetition typerCompetition = new TyperCompetition();
        typerCompetition.setId(id);
        return typerRoundRepository.findAllByTyperCompetition(typerCompetition);
    }

    @Override
    public TyperRound findLatestTyperRoundByTyperCompetition(TyperCompetition typerCompetition) throws ResourceNotFoundException {
        return findRoundByTyperCompetition(typerCompetition, typerCompetition.getCurrentRound());
    }

    @Override
    public TyperRound findRoundByTyperCompetition(TyperCompetition typerCompetition, Integer round) throws ResourceNotFoundException{
        Optional<TyperRound> optionalTyperRound = typerRoundRepository.findByTyperCompetitionAndNumber(typerCompetition, round);
        if (optionalTyperRound.isEmpty()) {
            ResourceNotFoundException ex = new ResourceNotFoundException();
            ex.setResource("TyperRound");
            throw ex;
        }
        return optionalTyperRound.get();
    }


    @Override
    public void deleteAll(List<TyperRound> typerRounds) {
        typerRoundRepository.deleteAll(typerRounds);
    }

    @Override
    public void saveAll(List<TyperRound> standings) {
        typerRoundRepository.saveAll(standings);
    }

    @Override
    public void createNewTyperRound(TyperCompetition typerCompetition) {
        TyperRound typerRound = new TyperRound(typerCompetition);
        save(typerRound);
    }


    @Override
    public TyperRound save(TyperRound typerRound) throws BadResourceException, ResourceAlreadyExistsException {
        if (typerRound.getTyperCompetition()!=null) {
            if (typerRound.getId() != null && existsById(typerRound.getId())) {
                ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("TyperRound with id: " + typerRound.getId());
                ex.setResource("TyperRound");
                ex.setIssue("id");
                throw ex;
            }
            return typerRoundRepository.save(typerRound);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save TyperRound");
            throw exc;
        }
    }

    @Override
    public TyperRound update(TyperRound typerRound) {

            if ((typerRound.getTyperCompetition() != null) && typerRound.getId()!=null && existsById(typerRound.getId())) {

            TyperRound typerRoundDB = findById(typerRound.getId());
            typerRoundDB.setNumber(typerRound.getNumber());
            typerRoundDB.setTyperMatches(typerRound.getTyperMatches());
            return typerRoundRepository.save(typerRoundDB);
        } else {
            BadResourceException exc = new BadResourceException("Failed to save TyperRound");
            throw exc;
        }
    }


    private TyperRound findById(Integer id) throws ResourceNotFoundException {
        Optional<TyperRound> typerRoundOptional = typerRoundRepository.findById(id);
        return typerRoundOptional.orElseThrow(() -> {
            ResourceNotFoundException ex = new ResourceNotFoundException("Cannot find TyperRound with id: " + id);
            ex.setResource("typerRound");
            return ex;
        });
    }

    private boolean existsById(Integer id) {
        return typerRoundRepository.existsById(id);
    }

}
