package the.xan.arkenstone.task.tracker.api.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.BoardDto;
import the.xan.arkenstone.task.tracker.api.services.BoardServiceImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@RestController
public class BoardController {

    private final BoardServiceImpl boardService;

    private final static String FETCH_BOARD = "/api/boards";
    private final static String CREATE_BOARD = "/api/boards";
    private final static String EDIT_BOARD = "/api/boards/{board_id}";
    private final static String DELETE_BOARD = "/api/boards/{board_id}";

    @GetMapping(FETCH_BOARD)
    public List<BoardDto> fetchBoard(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName
    ) {
        return boardService.fetchBoard(optionalPrefixName);
    }

    @PostMapping(CREATE_BOARD)
    public BoardDto createBoard(@RequestParam String name) {
        return boardService.createBoard(name);
    }

    @PatchMapping(EDIT_BOARD)
    public BoardDto editBoard(@PathVariable("board_id") Long boardId,
                              @RequestParam String name) {
        return boardService.editBoard(boardId, name);
    }

    @DeleteMapping(DELETE_BOARD)
    public AskDto deleteBoard(@PathVariable("board_id") Long boardId) {
        return boardService.deleteBoard(boardId);
    }
}
