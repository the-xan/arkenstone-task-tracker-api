package the.xan.arkenstone.task.tracker.api.model.factories;

import org.springframework.stereotype.Component;
import the.xan.arkenstone.task.tracker.api.model.dto.BoardDto;
import the.xan.arkenstone.task.tracker.store.entities.BoardEntity;
@Component
public class BoardDtoFactory {
    public BoardDto makeBoardDto(BoardEntity entity){

        return BoardDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

    }



}
