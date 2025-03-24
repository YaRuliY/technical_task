package technikal.task.fishmarket.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import technikal.task.fishmarket.models.entity.Fish;

public interface FishRepository extends JpaRepository<Fish, Integer> {}