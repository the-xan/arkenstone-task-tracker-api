package the.xan.arkenstone.task.tracker.api.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.xan.arkenstone.task.tracker.api.controllers.helpers.ControllerHelper;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.api.model.factories.TaskStateDtoFactory;
import the.xan.arkenstone.task.tracker.api.services.interfaces.TaskStateService;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;
import the.xan.arkenstone.task.tracker.store.repositories.TaskStateRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TaskStateServiceImpl implements TaskStateService {

    private final TaskStateDtoFactory taskStateDtoFactory;

    private final TaskStateRepository taskStateRepository;

    private final ControllerHelper controllerHelper;

    @Autowired
    public TaskStateServiceImpl(TaskStateDtoFactory taskStateDtoFactory,
                                TaskStateRepository taskStateRepository,
                                ControllerHelper controllerHelper) {
        this.taskStateDtoFactory = taskStateDtoFactory;
        this.taskStateRepository = taskStateRepository;
        this.controllerHelper = controllerHelper;
    }

    @Override
    public TaskStateDto createTaskState(Long projectId, String taskStateName) {
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

    @Override
    public List<TaskStateDto> getTaskStates(Long projectId, Optional<Long> optionalTaskStateId) {
        controllerHelper.getProjectOrThrowException(projectId);

        Stream<TaskStateEntity> projectStream = optionalTaskStateId
                .map(taskStateRepository::streamById)
                .orElseGet(taskStateRepository::streamAllBy);

        return projectStream
                .map(taskStateDtoFactory::makeTaskStateDto)
                .collect(Collectors.toList());
    }

    @Override
    public TaskStateDto updateTaskStateOrdinal(Long projectId, Long taskStateId, Integer newOrdinal) {
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

        if (newOrdinal < 0 || newOrdinal > taskStateRepository.findMaxOrdinalByProjectId(projectId) || currentOrdinal == newOrdinal) {
            throw new BadRequestException("Invalid ordinal");
        }

        if (newOrdinal < currentOrdinal) {
            taskStateRepository.incrementOrdinalsBetween(projectId, currentOrdinal, newOrdinal);
        }

        if (newOrdinal > currentOrdinal) {
            taskStateRepository.decrementOrdinalsBetween(projectId, currentOrdinal, newOrdinal);
        }

        taskState.setOrdinal(newOrdinal);
        taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }

    public TaskStateDto renameTaskState(Long projectId, Long taskStateId, String taskStateName) {

        if (taskStateName.isBlank()) {
            throw new BadRequestException("New task state name can`t be null!");
        }

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        TaskStateEntity taskState = project.getTaskStates()
                .stream()
                .filter(sameTaskState -> Objects.equals(sameTaskState.getId(), taskStateId))
                .findAny()
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t contains task state with \"%s\" id",
                                        projectId, taskStateId))
                );

        taskState.setName(taskStateName);

        taskStateRepository.saveAndFlush(taskState);

        return taskStateDtoFactory.makeTaskStateDto(taskState);
    }



    @Override
    public AskDto deleteTaskState(Long projectId, Long taskStateId) {

        ProjectEntity project = controllerHelper.getProjectOrThrowException(projectId);

        TaskStateEntity taskState = project.getTaskStates()
                .stream()
                .filter(sameTaskState -> Objects.equals(sameTaskState.getId(), taskStateId))
                .findAny()
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t contains task state with \"%s\" id",
                                        projectId, taskStateId))
                );

        taskStateRepository.deleteById(taskState.getId());
        taskStateRepository.decrementOrdinalsGreaterThan(projectId, taskState.getOrdinal());

        return AskDto.makeDefault(true);
    }
}
