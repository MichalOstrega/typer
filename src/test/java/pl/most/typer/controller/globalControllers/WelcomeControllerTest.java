package pl.most.typer.controller.globalControllers;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.most.typer.model.competition.Competition;
import pl.most.typer.model.dto.HeaderCompetitionListDTO;
import pl.most.typer.model.typer.TyperCompetition;
import pl.most.typer.service.typer.TyperCompetitionService;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WelcomeController.class)
class WelcomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HeaderCompetitionListDTO headerCompetitionListDTO;

    @MockBean
    private CommandLineRunner commandLineRunner;
    @MockBean
    private TyperCompetitionService typerCompetitionService;

    @Test
    void login() {
    }

    @Test
    void loginError() {
    }

    @Test
    void welcome() throws Exception {

        when(headerCompetitionListDTO.getCompetitions()).thenReturn(Arrays.asList(new Competition(2002,"Bundesliga")));
        when(typerCompetitionService.findAll()).thenReturn(Arrays.asList(new TyperCompetition("Liga Testowa")));


        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Witaj w świecie typerów")))
                .andExpect(MockMvcResultMatchers.model().hasNoErrors())
                .andExpect(MockMvcResultMatchers.model().attributeExists("navCompetitions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("navTyperCompetitions"));
    }

    @Test
    void about() {
    }
}

