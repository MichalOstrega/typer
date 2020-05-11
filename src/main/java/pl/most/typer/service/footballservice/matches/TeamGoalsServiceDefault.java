package pl.most.typer.service.footballservice.matches;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.most.typer.exceptions.BadResourceException;
import pl.most.typer.exceptions.ResourceAlreadyExistsException;
import pl.most.typer.exceptions.ResourceNotFoundException;
import pl.most.typer.model.matches.Score;
import pl.most.typer.model.matches.TeamGoals;
import pl.most.typer.repository.footballrepo.TeamGoalsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TeamGoalsServiceDefault implements TeamGoalsService {

    private TeamGoalsRepository teamGoalsRepository;

    public TeamGoalsServiceDefault(TeamGoalsRepository teamGoalsRepository) {
        this.teamGoalsRepository = teamGoalsRepository;
    }

    @Override
    public void saveAll(List<TeamGoals> teamGoalsFromMatchDTO) throws ResourceAlreadyExistsException {
        for (TeamGoals teamGoals : teamGoalsFromMatchDTO) {
            if (teamGoals.getId() != null && existById(teamGoals.getId())) {
                ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("TeamGoals with id: " +
                        teamGoals.getId() + " already exists");
                ex.setResource("TeamGoals");
                ex.setIssue("id");
            } else {
                teamGoalsRepository.save(teamGoals);
            }
        }
    }


    @Override
    public boolean existById(Integer id) {
        return teamGoalsRepository.existsById(id);
    }


}
