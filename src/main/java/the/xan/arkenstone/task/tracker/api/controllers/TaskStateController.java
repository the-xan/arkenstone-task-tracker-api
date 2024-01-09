package the.xan.arkenstone.task.tracker.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.api.services.TaskStateServiceImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class TaskStateController {

    private final TaskStateServiceImpl taskStateService;

    private final static String GET_TASK_STATES = "/api/projects/{project_id}/task-states";
    private final static String CREATE_TASK_STATES = "/api/projects/{project_id}/task-states";
    private final static String EDIT_TASK_STATE_ORDINAL = "/api/projects/{project_id}/task-states/{task_state_id}";
    private final static String DELETE_TASK_STATE = "/api/projects/{project_id}/task-states/{task_state_id}";
    private final static String EDIT_TASK_STATE_NAME = "/api/projects/{project_id}/task-states/{task_state_id}";

    @GetMapping(GET_TASK_STATES)
    public List<TaskStateDto> getTaskStates(@PathVariable(name = "project_id") Long projectId,
                                            @RequestParam(name = "task_state_id", required = false) Optional<Long> optionalTaskStateId) {
        return taskStateService
                .getTaskStates(projectId, optionalTaskStateId);
    }

    @PostMapping(CREATE_TASK_STATES)
    public TaskStateDto createTaskState(@PathVariable(value = "project_id") Long projectId,
                                        @RequestParam(name = "task_state_name") String taskStateName) {
        return taskStateService
                .createTaskState(projectId, taskStateName);
    }

    @Transactional
    @PatchMapping (EDIT_TASK_STATE_ORDINAL)
    public TaskStateDto updateTaskStateOrdinal(@PathVariable(value = "project_id") Long projectId,
                                               @PathVariable(value = "task_state_id") Long taskStateId,
                                               @RequestParam(name = "task_state_ordinal") Integer newOrdinal) {
        return taskStateService
                .updateTaskStateOrdinal(projectId, taskStateId, newOrdinal);
    }


    @PatchMapping (EDIT_TASK_STATE_NAME)
    public TaskStateDto updateTaskStateName(@PathVariable(value = "project_id") Long projectId,
                                               @PathVariable(value = "task_state_id") Long taskStateId,
                                               @RequestParam(name = "task_state_name") String taskStateName) {
        return taskStateService
                .renameTaskState(projectId,taskStateId,taskStateName);
    }


    @Transactional
    @DeleteMapping (DELETE_TASK_STATE)
    public AskDto deleteTaskState(@PathVariable(value = "project_id") Long projectId,
                                 @PathVariable(value = "task_state_id") Long taskStateId) {
        return taskStateService
                .deleteTaskState(projectId, taskStateId);
    }
}
