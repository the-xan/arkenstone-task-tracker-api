package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
    @Query("SELECT COALESCE(MAX(t.ordinal), 0) FROM TaskStateEntity t WHERE t.project.id = :projectId")
    Integer findMaxOrdinalByProjectId(Long projectId);
}
