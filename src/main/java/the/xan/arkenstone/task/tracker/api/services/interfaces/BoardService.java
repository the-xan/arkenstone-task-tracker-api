package the.xan.arkenstone.task.tracker.api.services.interfaces;


import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.BoardDto;


public interface BoardService {

    BoardDto createBoard(String name);

    BoardDto editBoard(Long projectId, String name);

    AskDto deleteBoard(Long projectId);
}
