package technikal.task.fishmarket.conf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Security {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.requestMatchers("/createUser", "/login")
                                               .permitAll()
                                               .anyRequest()
                                               .authenticated())
            .formLogin(form -> form.loginPage("/login").permitAll().defaultSuccessUrl("/fish"))
            .logout(LogoutConfigurer::permitAll)
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder defaultEncoder() {
        return new BCryptPasswordEncoder();
    }
}