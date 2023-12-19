package the.xan.arkenstone.task.tracker.api.controllers;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import the.xan.arkenstone.task.tracker.api.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.api.factories.ProjectDtoFactory;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.repositories.ProjectRepository;

import java.util.Objects;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
@RestController
public class ProjectController {
    ProjectDtoFactory projectDtoFactory;

    ProjectRepository projectRepository;

    private final static String CREATE_PROJECT = "/api/projects";
    private final static String EDIT_PROJECT = "/api/projects/{project_id}";

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {

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

    @PostMapping(EDIT_PROJECT)
    public ProjectDto editPatch(@PathVariable("project_id") Long projectId,
                                  @RequestParam String name) {

        if(name.trim().isEmpty()) {
            throw new BadRequestException("New name can`t be empty");
        }

        ProjectEntity project = projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                    new NotFoundException(String.format("Project with \"%s\" id, doesn`t exist", projectId))
                );

        projectRepository
                .findByName(name)
                .filter(anotherProject -> !Objects.equals(anotherProject.getId(), projectId))
                .ifPresent(anotherProject  -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists", name));
                });

        project.setName(name);

        projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }
}
