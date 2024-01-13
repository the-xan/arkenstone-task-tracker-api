package the.xan.arkenstone.task.tracker.api.controllers.helpers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.store.entities.BoardEntity;
import the.xan.arkenstone.task.tracker.store.repositories.BoardRepository;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    BoardRepository boardRepository;

    public BoardEntity getBoardOrThrowException(Long projectId) {
        return boardRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t exist", projectId))
                );
    }


}
