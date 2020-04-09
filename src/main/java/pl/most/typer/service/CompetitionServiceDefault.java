package pl.most.typer.service;

import org.springframework.stereotype.Service;
import pl.most.typer.model.league.Competition;
import pl.most.typer.repository.CompetitionRepository;

import java.util.Optional;


@Service
public class CompetitionServiceDefault implements CompetitionService {

    private CompetitionRepository competitionRepository;

    public CompetitionServiceDefault(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    @Override
    public Optional<Competition> findByApiId(Integer apiId) {
        return competitionRepository.findByApiId(apiId);
    }

    @Override
    public boolean existsCompetitionByApiId(Integer apiId) {
        return competitionRepository.existsCompetitionByApiId(apiId);
    }

    @Override
    public Competition save(Competition competition) {
        return competitionRepository.save(competition);
    }
}