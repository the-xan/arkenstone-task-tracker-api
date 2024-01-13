package the.xan.arkenstone.task.tracker.api.services;


import org.springframework.stereotype.Service;
import the.xan.arkenstone.task.tracker.api.controllers.helpers.ControllerHelper;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.BoardDto;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.model.factories.BoardDtoFactory;
import the.xan.arkenstone.task.tracker.api.services.interfaces.BoardService;
import the.xan.arkenstone.task.tracker.store.entities.BoardEntity;
import the.xan.arkenstone.task.tracker.store.repositories.BoardRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service()
public class BoardServiceImpl implements BoardService {

    private final BoardDtoFactory boardDtoFactory;

    private final BoardRepository boardRepository;

    private final ControllerHelper controllerHelper;


    public BoardServiceImpl(BoardDtoFactory boardDtoFactory,
                            BoardRepository boardRepository,
                            ControllerHelper controllerHelper) {
        this.boardDtoFactory = boardDtoFactory;
        this.boardRepository = boardRepository;
        this.controllerHelper = controllerHelper;
    }

    @Override
    public BoardDto createBoard(String name) {
        if(name.trim().isEmpty()) {
            throw new BadRequestException("Name can`t be empty");
        }

        boardRepository.findByName(name)
                .ifPresent(boardEntity -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists", name));
                });

        BoardEntity project = boardRepository.saveAndFlush(
                BoardEntity.builder()
                        .name(name)
                        .build()
        );

        return boardDtoFactory.makeBoardDto(project);
    }

    @Override
    public BoardDto editBoard(Long boardId, String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("New name can`t be empty");
        }

        BoardEntity board = controllerHelper.getBoardOrThrowException(boardId);

        boardRepository
                .findByName(name)
                .filter(anotherBoard -> Objects.equals(anotherBoard.getId(), boardId))
                .ifPresent(anotherBoard -> {
                    throw new BadRequestException(String.format("Project with name \"%s\" already exists", name));
                });

        board.setName(name);

        boardRepository.saveAndFlush(board);


        return boardDtoFactory.makeBoardDto(board);
    }

    @Override
    public AskDto deleteBoard(Long boardId) {
        controllerHelper.getBoardOrThrowException(boardId);

        boardRepository.deleteById(boardId);

        return AskDto.makeDefault(true);
    }

    public List<BoardDto> fetchBoard(Optional<String> optionalPrefixName) {
        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<BoardEntity> boardStream = optionalPrefixName
                .map(boardRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(boardRepository::streamAllBy);
        return boardStream.map(boardDtoFactory::makeBoardDto).collect(Collectors.toList());
    }
}
