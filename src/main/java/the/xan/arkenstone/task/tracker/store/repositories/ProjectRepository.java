package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;

import java.util.Optional;

public interface ProjectRepository  extends JpaRepository<ProjectEntity, Long> {
    Optional<ProjectEntity> findByName(String name);
}
