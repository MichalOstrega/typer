package pl.most.typer.service.typer;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.exceptions.ResourceException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.model.typer.TyperPlayer;
import pl.most.typer.model.typer.TyperRound;
import pl.most.typer.model.typer.TyperStanding;
import pl.most.typer.repository.typerrepo.TyperCompetitionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TyperCompetitionServiceDefault implements TyperCompetitionService {

    private TyperCompetitionRepository typerCompetitionRepository;
    private TyperStandingService typerStandingService;
    private TyperPlayerServiceDefault typerPlayerServiceDefault;
    private TyperRoundService typerRoundService;

    public TyperCompetitionServiceDefault(TyperCompetitionRepository typerCompetitionRepository,
                                          TyperStandingService typerStandingService, TyperLeagueStandingService typerLeagueStandingService, TyperPlayerServiceDefault playerServiceDefault, TyperRoundService typerRoundService) {
        this.typerCompetitionRepository = typerCompetitionRepository;
        this.typerStandingService = typerStandingService;
        this.typerPlayerServiceDefault = playerServiceDefault;
        this.typerRoundService = typerRoundService;
    }

    private boolean existsById(Integer id) {
        return typerCompetitionRepository.existsById(id);
    }

    private boolean existsByName(String name) {
        return typerCompetitionRepository.existsByName(name);
    }

    @Override
    public TyperCompetition findById(Integer id) throws ResourceNotFoundException {
        Optional<TyperCompetition> typerCompetitionOptional = typerCompetitionRepository.findById(id);
        return typerCompetitionOptional.orElseThrow(() -> {
            ResourceNotFoundException ex = new ResourceNotFoundException("Cannot find TyperCompetition with id: " + id);
            ex.setResource("competition");
            return ex;
        });
    }

    @Override
    public List<TyperCompetition> findAll(int pageNumber, int rowPerPage) {
        List<TyperCompetition> typerCompetitions = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage,
                Sort.by("id").ascending());
        typerCompetitionRepository.findAll(sortedByIdAsc).forEach(typerCompetitions::add);
        return typerCompetitions;
    }

    @Override
    public List<TyperCompetition> findAll() {
        return typerCompetitionRepository.findAll();
    }

    @Override
    public TyperCompetition save(TyperCompetition typerCompetition) throws BadResourceException, ResourceAlreadyExistsException {
        if (!StringUtils.isEmpty(typerCompetition.getName())) {
            if (typerCompetition.getId() != null && existsById(typerCompetition.getId())) {
                ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("TyperCompetition with id: " + typerCompetition.getId() +
                        " and name: " + typerCompetition.getName() + " already exists");
                ex.setResource("Competition");
                ex.setIssue("id");
                throw ex;
            }
            else if (existsByName(typerCompetition.getName())){
                ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("TyperCompetition with name: \"" + typerCompetition.getName() + "\" already exists");
                ex.setResource("Competition");
                ex.setIssue("name");
                throw ex;
            }
            TyperCompetition competition = typerCompetitionRepository.save(typerCompetition);
            countRound(competition.getId());
            return competition;
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save TyperCompetition");
            throw exc;
        }
    }

    @Override
    public void update(TyperCompetition typerCompetition)
            throws BadResourceException, ResourceNotFoundException {
        if (!StringUtils.isEmpty(typerCompetition.getName())) {
            if (!existsById(typerCompetition.getId())) {
                ResourceNotFoundException ex = new ResourceNotFoundException("Cannot find TyperCompetition with id: " + typerCompetition.getId());
                ex.setResource("Competition");
                ex.setIssue("id");
                throw ex;
            }

            else if (!findById(typerCompetition.getId()).equals(findByName(typerCompetition.getName()))){
                ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("TyperCompetition with name: \"" + typerCompetition.getName() + "\" already exists");
                ex.setResource("Competition");
                ex.setIssue("name");
                throw ex;
            }
            TyperCompetition typerCompetitionDB = typerCompetitionRepository.findById(typerCompetition.getId()).get();
            typerCompetitionDB.setName(typerCompetition.getName());
            typerCompetitionDB.setCurrentRound(typerCompetition.getCurrentRound());
            typerCompetitionDB.setLastUpdated(typerCompetition.getLastUpdated());
            typerCompetitionDB.setTyperPlayers(typerCompetition.getTyperPlayers());
            typerCompetitionRepository.save(typerCompetitionDB);
        }
        else {
            BadResourceException exc = new BadResourceException("Failed to save typerCompetition");
            throw exc;
        }
    }

    private TyperCompetition findByName(String name) {
        Optional<TyperCompetition> typerCompetitionOptional = typerCompetitionRepository.findByName(name);
        return typerCompetitionOptional.orElseThrow(() -> {
            ResourceNotFoundException ex = new ResourceNotFoundException("Cannot find TyperCompetition with name: " + name);
            ex.setResource("competition");
            return ex;
        });
    }

    @Override
    public void deleteById(Integer id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            ResourceNotFoundException ex = new ResourceNotFoundException("Cannot find typerCompetition with id: " + id);
            ex.setIssue("id");
            throw ex;
        }
        else {
            List<TyperStanding> typerStandings = typerStandingService.findAllByTyperCompetitionId(id);
            typerStandingService.deleteAll(typerStandings);
            List<TyperRound> typerRounds = typerRoundService.findAllByTyperCompetitionId(id);
            typerRoundService.deleteAll(typerRounds);
            typerCompetitionRepository.deleteById(id);
        }
    }

    @Override
    public Long count() {
        return typerCompetitionRepository.count();
    }

    @Override
    public void deletePlayerFromCompetition(Integer competitionId, Integer playerId) {
        TyperCompetition typerCompetition = findById(competitionId);
        TyperPlayer typerPlayer =typerPlayerServiceDefault.findById(playerId);
        typerCompetition.removeTyperPlayer(typerPlayer);
        update(typerCompetition);
        typerPlayerServiceDefault.update(typerPlayer);
    }

    @Override
    public void addPlayerToCompetition(Integer competitionId, Integer playerId) {
        TyperCompetition typerCompetition = findById(competitionId);
        TyperPlayer typerPlayer =typerPlayerServiceDefault.findById(playerId);
        typerCompetition.addTyperPlayer(typerPlayer);
        update(typerCompetition);
        typerPlayerServiceDefault.update(typerPlayer);
    }

    @Override
    public void countRound(Integer competitionId) {
        TyperCompetition typerCompetition = findById(competitionId);
        TyperStanding latestStandingByTyperCompetition;
        try {
            latestStandingByTyperCompetition = typerStandingService.findLatestStandingByTyperCompetition(typerCompetition);
        }
        catch (ResourceException ex)
        {
            latestStandingByTyperCompetition = null;
        }
        //TODO policzyc punkty za kolejke i dodac do tabeli -> usunac try catch

        //stworzenie nowej tabeli dla nastepnej kolejki
        typerStandingService.createNewTyperStanding(typerCompetition, latestStandingByTyperCompetition);
        typerRoundService.createNewTyperRound(typerCompetition);
        typerCompetition.setLastUpdated(LocalDateTime.now());
        update(typerCompetition);
    }
}
