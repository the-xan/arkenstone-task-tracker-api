package the.xan.arkenstone.task.tracker.api.controllers;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.api.factories.ProjectDtoFactory;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.repositories.ProjectRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
@RestController
public class ProjectController {
    ProjectDtoFactory projectDtoFactory;

    ProjectRepository projectRepository;

    private final static String FETCH_PROJECT = "/api/projects";
    private final static String CREATE_PROJECT = "/api/projects";
    private final static String EDIT_PROJECT = "/api/projects/{project_id}";
    private final static String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> fetchProject(@RequestParam(value = "prefix_name", required = false)
                                         Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAll);

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }
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

    @PatchMapping (EDIT_PROJECT)
    public ProjectDto editPatch(@PathVariable("project_id") Long projectId,
                                  @RequestParam String name) {

        if(name.trim().isEmpty()) {
            throw new BadRequestException("New name can`t be empty");
        }

        ProjectEntity project = getProjectOrThrowException(projectId);

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
    @DeleteMapping(DELETE_PROJECT)
    public AskDto deleteProject(@PathVariable("project_id") Long projectId) {

        getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AskDto.makeDefault(true);
    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {
       return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t exist", projectId))
                );
    }

}
