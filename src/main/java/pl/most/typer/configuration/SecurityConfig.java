package pl.most.typer.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.most.typer.model.account.RoleType;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Autowired
    private UserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                /**
                 * Autoryzacja ścieżek dostępu
                 */
                .authorizeRequests()
                .antMatchers("/rest/hello/secured/**").hasRole(RoleType.ADMIN.name())
                .antMatchers("/", "/**").permitAll()
                /**
                 * Formularz logowania
                 */
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                //domyślna strona po zalogowaniu
                .defaultSuccessUrl("/")
                //Obsługa wylogowania
                .and()
                .logout()
                .logoutSuccessUrl("/")
                /**
                 * Autoryzacja dostępu do bazy danych
                 */
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                .and()
                .exceptionHandling()
                .accessDeniedPage("/error");
    }
}
