package pl.most.typer.service.footballservice.competition;

import org.springframework.stereotype.Service;
import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.exceptions.UpToDateResourceException;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.repository.footballrepo.CompetitionRepository;

import java.util.List;
import java.util.Optional;


@Service
public class CompetitionServiceDefault implements CompetitionService {

    private CompetitionRepository competitionRepository;

    public CompetitionServiceDefault(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @Override
    public boolean existsByApiId(Integer apiId) {
        return competitionRepository.existsByApiId(apiId);
    }

    @Override
    public Competition findByApiId(Integer apiId) throws ResourceNotFoundException{
        Optional<Competition> competitionOptional = competitionRepository.findByApiId(apiId);
        return competitionOptional.orElseThrow(() -> {
            ResourceNotFoundException ex = new ResourceNotFoundException("Cannot find Competition with id: " + apiId);
            ex.setResource("competition");
            return ex;
        });
    }

    @Override
    public Competition save(Competition competition) {
        if (!existsByApiId(competition.getApiId())) {
            return competitionRepository.save(competition);
        }
        else {
            ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("Competition with id: " + competition.getApiId() +
                    " and name: " + competition.getName() + " already exists");
            ex.setResource("Competition");
            ex.setIssue("id");
            throw ex;
        }
    }

    @Override
    public Competition update(Competition competition) throws BadResourceException, ResourceNotFoundException {
        Competition competitionDB = findByApiId(competition.getApiId());
        if (!competitionDB.getLastUpdated().isEqual(competition.getLastUpdated())) {
            competitionDB.setPlan(competition.getPlan());
            competitionDB.setCode(competition.getCode());
            competitionDB.setName(competitionDB.getPlan());
            competitionDB.setLastUpdated(competition.getLastUpdated());
            return competitionRepository.save(competitionDB);
        } else {
            UpToDateResourceException exc = new UpToDateResourceException("Competition is up-to-date");
            throw exc;
        }
    }



    @Override
    public Competition getCompetition(Integer id) {
        return findByApiId(id);
    }

    @Override
    public String getCompetitionName(Integer id) {
        return findByApiId(id).getName();
    }

    @Override
    public List<Competition> getAll() {
        return competitionRepository.findAll();
    }

    @Override
    public boolean isCompetitionInDBUpToDate(Competition competition) {
        Competition competitionDB = findByApiId(competition.getApiId());
        return competitionDB.getLastUpdated().isEqual(competition.getLastUpdated());
    }


}
