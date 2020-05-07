package pl.most.typer.service.accountservice;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.most.typer.model.account.PlayerDTO;
import pl.most.typer.model.account.User;
import pl.most.typer.repository.accountrepo.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User '" + username + "' not found!"));
    }

    public boolean isUserPresent(String username, String mail) {
        Optional<User> checkIfExist = userRepository.findByUsernameOrMail(username, mail);
        return checkIfExist.isPresent();
    }

    public boolean isUserPresent(String username) {
        Optional<User> checkIfExist = userRepository.findByUsername(username);
        return checkIfExist.isPresent();
    }



}
