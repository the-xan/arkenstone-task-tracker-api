package the.xan.arkenstone.task.tracker.api.factories;

import org.springframework.stereotype.Component;
import the.xan.arkenstone.task.tracker.api.dto.TaskDto;
import the.xan.arkenstone.task.tracker.api.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.store.entities.TaskEntity;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;
@Component
public class TaskDtoFactory {
    public TaskDto makeTaskDto(TaskEntity entity){

        return TaskDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
        
    }
}
