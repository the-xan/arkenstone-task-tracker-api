package the.xan.arkenstone.task.tracker.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.controllers.helpers.ControllerHelper;
import the.xan.arkenstone.task.tracker.api.model.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.api.model.factories.TaskStateDtoFactory;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;
import the.xan.arkenstone.task.tracker.store.repositories.TaskStateRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
public class TaskStateController {

    TaskStateDtoFactory taskStateDtoFactory;

    TaskStateRepository taskStateRepository;

    ControllerHelper controllerHelper;

    private final static String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private final static String CREATE_TASK_STATES = "/api/projects/{project_id}/task-states";
    private final static String EDIT_TASK_STATE_ORDINAL = "/api/projects/{project_id}/task-states/{task_state_id}";

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
            ordinal = taskStateRepository.findMaxOrdinalByProjectId(projectId) + 1;
        }

        TaskStateEntity taskState = taskStateRepository.saveAndFlush(
                TaskStateEntity.builder()
                        .name(taskStateName)
                        .ordinal(ordinal)
                        .project(project)
                        .build());

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

    @Transactional
    @PatchMapping (EDIT_TASK_STATE_ORDINAL)
    public TaskStateDto updateTaskStateOrdinal(@PathVariable(value = "project_id") Long projectId,
                                            @PathVariable(value = "task_state_id") Long taskStateId,
                                            @RequestParam(name = "task_state_ordinal") Integer newOrdinal) {

        // проверить что переданный порядковый номер не пустой
        if (newOrdinal == null) {
            throw new BadRequestException("New task state ordinal can`t be null!");
        }

        // проверить что проект существует
        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        // проверить что переданный таск стейт существует в рамках проекта
        TaskStateEntity taskState = project.getTaskStates()
                .stream()
                .filter(sameTaskState -> Objects.equals(sameTaskState.getId(), taskStateId))
                .findAny()
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t contains task state with \"%s\" id",
                                        projectId, taskStateId))
                );

        // Присвоение нового порядкового номера
        int currentOrdinal = taskState.getOrdinal();

        if (newOrdinal < 0 || newOrdinal > taskStateRepository.findMaxOrdinalByProjectId(projectId) || taskState.getOrdinal() == newOrdinal) {
            throw new BadRequestException("Invalid ordinal");
        }

        if (newOrdinal < currentOrdinal) {
            taskStateRepository.incrementOrdinalsBetween(projectId, currentOrdinal, newOrdinal);
        } else if (newOrdinal > currentOrdinal) {
            taskStateRepository.decrementOrdinalsBetween(projectId, currentOrdinal, newOrdinal);
        }

        taskState.setOrdinal(newOrdinal);
        taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

}
