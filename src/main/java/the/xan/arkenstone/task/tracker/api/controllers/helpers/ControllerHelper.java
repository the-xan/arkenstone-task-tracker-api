package the.xan.arkenstone.task.tracker.api.controllers.helpers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.repositories.ProjectRepository;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    ProjectRepository projectRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t exist", projectId))
                );
    }


}
