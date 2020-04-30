package pl.most.typer.repository.typerrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.model.typer.TyperRound;
import pl.most.typer.model.typer.TyperRound;

import java.util.List;
import java.util.Optional;

public interface TyperRoundRepository extends JpaRepository<TyperRound, Integer> {

    List<TyperRound> findAllByTyperCompetition(TyperCompetition Id);


    Optional<TyperRound> findByTyperCompetitionAndNumber(TyperCompetition typerCompetition, Integer number);
}
