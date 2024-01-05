package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
    Optional<TaskStateEntity> findByName(String name);

    @Query("SELECT COALESCE(MAX(c.ordinal), 0) FROM TaskStateEntity c")
    Integer findMaxOrderIndex();


}
