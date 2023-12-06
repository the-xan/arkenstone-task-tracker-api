package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;

public interface ProjectRepository  extends JpaRepository<ProjectEntity, Long> {
}
