package pl.most.typer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import pl.most.typer.controller.footballController.CompetitionController;
import pl.most.typer.controller.footballController.MatchController;
import pl.most.typer.model.account.Role;
import pl.most.typer.model.account.RoleType;
import pl.most.typer.model.account.User;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.dto.HeaderCompetitionListDTO;
import pl.most.typer.model.typer.*;
import pl.most.typer.repository.accountrepo.RoleRepository;
import pl.most.typer.repository.accountrepo.UserRepository;
import pl.most.typer.repository.typerrepo.*;
import pl.most.typer.service.footballservice.competition.LeagueService;
import pl.most.typer.service.footballservice.matches.MatchesService;
import pl.most.typer.service.typer.TyperCompetitionService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
public class TyperApplication {
    ///First commit
    public static void main(String[] args) {
        SpringApplication.run(TyperApplication.class, args);
    }


    @Bean
    public CommandLineRunner dataLoader(
            UserRepository userRepository,
            RoleRepository roleRepository,
            TyperCompetitionRepository typerCompetitionRepository,
            TyperPlayerRepository typerPlayerRepository,
            TyperStandingRepository typerStandingRepository,
            TyperRoundRepository typerRoundRepository,
            HeaderCompetitionListDTO headerCompetitionListDTO,
            LeagueService leagueService,
            MatchesService matchesService
    ) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                initializeRoleTypesInDb();

                //Generate default user
                User admin = createUser("admin", RoleType.ADMIN);
                User user1 = createUser("user1");
                User user2 = createUser("user2");
                List<User> users = Arrays.asList(admin, user1, user2);
                userRepository.saveAll(users);

                TyperCompetition typerCompetition = new TyperCompetition("Liga Testowa");

                List<TyperPlayer> typerPlayers = users.stream().map(user -> getTyperPlayer(user)).collect(Collectors.toList());
                typerPlayers.stream().forEach(typerPlayer -> typerPlayer.addTyperCompetition(typerCompetition));

                TyperStanding typerStanding = new TyperStanding(typerCompetition);
                TyperRound typerRound = new TyperRound(typerCompetition);

                typerPlayerRepository.saveAll(typerPlayers);
                typerCompetitionRepository.save(typerCompetition);
                typerStandingRepository.save(typerStanding);
                typerRoundRepository.save(typerRound);


                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (Competition competition : headerCompetitionListDTO.getCompetitions()) {
                            try {
                                leagueService.getStandingInfoFromExternalApi(competition.getApiId());
                                log.info(LocalDateTime.now() + " - " + competition.getName() + " standings saved successful");
                                Thread.sleep(6000);
                                matchesService.getMatchInfoFromExternalApi(competition.getApiId());
                                log.info(LocalDateTime.now() + " - " + competition.getName() + " matches saved successful");
                                Thread.sleep(6000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                new Thread(runnable).run();
            }

            private TyperPlayer getTyperPlayer(User user) {
                TyperPlayer typerPlayer = new TyperPlayer();
                typerPlayer.setUser(user);
                return typerPlayer;
            }

            private User createUser(String username, RoleType roleType) {
                User s = User.createUser(
                        username,
                        new BCryptPasswordEncoder().encode("1111"),
                        "test@test.pl"
                );
                s.setEnabled(true);
                s.getRoles().add(roleRepository.findByRoleType(roleType));
                return s;
            }

            private User createUser(String username) {
                return createUser(username, RoleType.USER);
            }

            private void initializeRoleTypesInDb() {
                for (RoleType roleType : RoleType.values()) {
                    Role role = new Role(roleType);
                    roleRepository.save(role);
                }
            }
        };


    }
}

