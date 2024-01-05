package the.xan.arkenstone.task.tracker.api.controllers;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.controllers.helpers.ControllerHelper;
import the.xan.arkenstone.task.tracker.api.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.factories.TaskStateDtoFactory;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;
import the.xan.arkenstone.task.tracker.store.repositories.TaskStateRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class TaskStateController {

    TaskStateDtoFactory taskStateDtoFactory;

    TaskStateRepository taskStateRepository;

    ControllerHelper controllerHelper;

    private final static String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private final static String CREATE_TASK_STATES = "/api/projects/{project_id}/task-states";
    private final static String EDIT_PROJECT = "/api/projects/{project_id}";
    private final static String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable(name = "project_id") Long projectId) {
        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        return project
                .getTaskStates()
                .stream()
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_TASK_STATES)
    public TaskStateDto createTaskState(@PathVariable(value = "project_id") Long projectId,
                                        @RequestParam(name = "task_state_name") String taskStateName) {

        if (taskStateName.isBlank()) {
            throw new BadRequestException("Task state name can`t be empty");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        project.getTaskStates()
                .stream()
                .filter(sameTaskStateName -> Objects.equals(sameTaskStateName.getName(), taskStateName))
                .findAny()
                .ifPresent(sameTaskStateName -> {
                    throw new BadRequestException(String.format("Task state with name \"%s\" already exists", taskStateName));
                });

        int ordinal = 0;

        if (!project.getTaskStates().isEmpty()) {
            ordinal = taskStateRepository.findMaxOrdinalValue() + 1;
        }

        TaskStateEntity taskState = taskStateRepository.saveAndFlush(
                TaskStateEntity.builder()
                        .name(taskStateName)
                        .ordinal(ordinal)
                        .project(project)
                        .build());

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }
}
