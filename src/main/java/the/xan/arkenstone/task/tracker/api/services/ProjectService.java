package the.xan.arkenstone.task.tracker.api.services;

import org.springframework.stereotype.Service;
import the.xan.arkenstone.task.tracker.api.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;

public interface ProjectService{
    ProjectDto createProject(String name);
}
@Service
class ProjectServiceImpl implements ProjectService {

    @Override
    public ProjectDto createProject(String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("Name can`t be empty");
        }

        projectRepository.findByName(name)
                .ifPresent(projectEntity -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists", name));
                });

        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );

        return projectDtoFactory.makeProjectDto(project);
    }
}
