package the.xan.arkenstone.task.tracker.api.model.factories;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import the.xan.arkenstone.task.tracker.api.model.dto.ColumnDto;
import the.xan.arkenstone.task.tracker.store.entities.ColumnEntity;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class ColumnDtoFactory {

    TaskDtoFactory taskDtoFactory;

    public ColumnDto makeColumnDto(ColumnEntity entity){

        return ColumnDto.builder()
                .id(entity.getId())
                .ordinal((long) entity.getOrdinal())
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
