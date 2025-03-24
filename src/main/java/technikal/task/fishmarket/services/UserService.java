package technikal.task.fishmarket.services;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import technikal.task.fishmarket.models.dto.UserDto;
import technikal.task.fishmarket.models.entity.Role;
import technikal.task.fishmarket.models.entity.User;
import technikal.task.fishmarket.repo.UserRepository;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder encoder;
    private final UserRepository users;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    public void save(UserDto dto, Role role) {
        log.info("Saving user {}", dto);
        Optional<User> opt = users.findByUsername(dto.getUsername());
        if (opt.isPresent()) {
            throw new RuntimeException("Already existing user");
        } else {
            User user = new User();
            user.setUsername((dto.getUsername()));
            user.setPassword(encoder.encode((dto.getPassword())));
            user.setRole(role);
            users.save(user);
        }
    }

    @PostConstruct
    public void saveUsersAfterInitialization() {
        this.save(new UserDto("admin", "admin"), Role.ADMIN);
        this.save(new UserDto("user", "user"), Role.USER);
    }
}