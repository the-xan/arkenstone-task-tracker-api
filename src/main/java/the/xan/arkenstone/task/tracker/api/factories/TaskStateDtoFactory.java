package the.xan.arkenstone.task.tracker.api.factories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import the.xan.arkenstone.task.tracker.api.dto.TaskStateDto;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TaskStateDtoFactory {

    TaskDtoFactory taskDtoFactory;

    public TaskStateDto makeTaskStateDto(TaskStateEntity entity){

        return TaskStateDto.builder()
                .id(entity.getId())
                .ordinal(entity.getOrdinal())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .tasks(
                        entity.getTasks()
                                .stream()
                                .map(taskDtoFactory::makeTaskDto)
                                .collect(Collectors.toList()))
                .build();
        
    }



}
