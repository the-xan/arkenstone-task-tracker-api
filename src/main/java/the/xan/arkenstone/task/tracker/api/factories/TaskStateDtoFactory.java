package the.xan.arkenstone.task.tracker.api.factories;

import org.springframework.stereotype.Component;
import the.xan.arkenstone.task.tracker.api.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.api.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;
@Component
public class TaskStateDtoFactory {
    public TaskStateDto makeTaskStateDto(TaskStateEntity entity){

        return TaskStateDto.builder()
                .id(entity.getId())
                .ordinal(entity.getOrdinal())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .build();
        
    }



}
