package technikal.task.fishmarket.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.models.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}