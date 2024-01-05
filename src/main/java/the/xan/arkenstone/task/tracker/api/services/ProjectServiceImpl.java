package the.xan.arkenstone.task.tracker.api.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.xan.arkenstone.task.tracker.api.controllers.helpers.ControllerHelper;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.model.factories.ProjectDtoFactory;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.repositories.ProjectRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class ProjectServiceImpl implements ProjectService {
    ProjectDtoFactory projectDtoFactory;

    ProjectRepository projectRepository;

    ControllerHelper controllerHelper;

    @Override
    public ProjectDto createProject(String name) {
        if(name.trim().isEmpty()) {
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

    @Override
    public ProjectDto editProject(Long projectId, String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("New name can`t be empty");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        projectRepository
                .findByName(name)
                .filter(anotherProject -> Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(anotherProject -> {
                    throw new BadRequestException(String.format("Project with name \"%s\" already exists", name));
                });

        project.setName(name);

        projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @Override
    public AskDto deleteProject(Long projectId) {
        controllerHelper.getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AskDto.makeDefault(true);
    }

    public List<ProjectDto> fetchProject(Optional<String> optionalPrefixName) {
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);
        return projectStream.map(projectDtoFactory::makeProjectDto).collect(Collectors.toList());
    }
}
