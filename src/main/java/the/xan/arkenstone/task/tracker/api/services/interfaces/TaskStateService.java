package the.xan.arkenstone.task.tracker.api.services.interfaces;

import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.TaskStateDto;

import java.util.List;
import java.util.Optional;

public interface TaskStateService {
    TaskStateDto createTaskState(Long projectId, String taskStateName);

    List<TaskStateDto> getTaskStates(Long projectId, Optional<Long> optionalTaskStateId);

    TaskStateDto updateTaskStateOrdinal(Long projectId,Long taskStateId,Integer newOrdinal);

    AskDto deleteTaskState(Long projectId, Long taskStateId);

}
