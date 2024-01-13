package the.xan.arkenstone.task.tracker.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import the.xan.arkenstone.task.tracker.api.controllers.helpers.ControllerHelper;
import the.xan.arkenstone.task.tracker.api.exceptions.BadRequestException;
import the.xan.arkenstone.task.tracker.api.exceptions.NotFoundException;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.ColumnDto;
import the.xan.arkenstone.task.tracker.api.model.factories.ColumnDtoFactory;
import the.xan.arkenstone.task.tracker.api.services.interfaces.ColumnService;
import the.xan.arkenstone.task.tracker.store.entities.BoardEntity;
import the.xan.arkenstone.task.tracker.store.entities.ColumnEntity;
import the.xan.arkenstone.task.tracker.store.repositories.ColumnRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnDtoFactory columnDtoFactory;

    private final ColumnRepository columnRepository;

    private final ControllerHelper controllerHelper;

    @Autowired
    public ColumnServiceImpl(ColumnDtoFactory columnDtoFactory,
                             ColumnRepository columnRepository,
                             ControllerHelper controllerHelper) {
        this.columnDtoFactory = columnDtoFactory;
        this.columnRepository = columnRepository;
        this.controllerHelper = controllerHelper;
    }

    @Override
    public ColumnDto createColumn(Long boardId, String columnName) {
        if (columnName.isBlank()) {
            throw new BadRequestException("Task state name can`t be empty");
        }

        BoardEntity board = controllerHelper.getBoardOrThrowException(boardId);

        board.getColumns()
                .stream()
                .filter(sameColumnName -> Objects.equals(sameColumnName.getName(), columnName))
                .findAny()
                .ifPresent(sameColumnName -> {
                    throw new BadRequestException(String.format("Task state with name \"%s\" already exists", columnName));
                });

        int ordinal = 0;

        if (!board.getColumns().isEmpty()) {
            ordinal = columnRepository.findMaxOrdinalByBoardId(boardId) + 1;
        }

        ColumnEntity taskState = columnRepository.saveAndFlush(
                ColumnEntity.builder()
                        .name(columnName)
                        .ordinal(ordinal)
                        .board(board)
                        .build());

        return columnDtoFactory.makeColumnDto(taskState);
    }

    @Override
    public List<ColumnDto> getColumns(Long boardId, Optional<Long> optionalColumnId) {
        controllerHelper.getBoardOrThrowException(boardId);

        Stream<ColumnEntity> boardStream = optionalColumnId
                .map(columnRepository::streamById)
                .orElseGet(columnRepository::streamAllBy);

        return boardStream
                .map(columnDtoFactory::makeColumnDto)
                .collect(Collectors.toList());
    }

    @Override
    public ColumnDto updateColumnOrdinal(Long boardId, Long columnId, Integer newOrdinal) {
        // проверить что переданный порядковый номер не пустой
        if (newOrdinal == null) {
            throw new BadRequestException("New task state ordinal can`t be null!");
        }

        // проверить что проект существует
        BoardEntity board = controllerHelper.getBoardOrThrowException(boardId);

        // проверить что переданный таск стейт существует в рамках проекта
        ColumnEntity column = board.getColumns()
                .stream()
                .filter(sameColumn -> Objects.equals(sameColumn.getId(), columnId))
                .findAny()
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t contains task state with \"%s\" id",
                                        boardId, columnId))
                );

        // Присвоение нового порядкового номера
        int currentOrdinal = column.getOrdinal();

        if (newOrdinal < 0 || newOrdinal > columnRepository.findMaxOrdinalByBoardId(boardId) || currentOrdinal == newOrdinal) {
            throw new BadRequestException("Invalid ordinal");
        }

        if (newOrdinal < currentOrdinal) {
            columnRepository.incrementOrdinalsBetween(boardId, currentOrdinal, newOrdinal);
        }

        if (newOrdinal > currentOrdinal) {
            columnRepository.decrementOrdinalsBetween(boardId, currentOrdinal, newOrdinal);
        }

        column.setOrdinal(newOrdinal);
        columnRepository.saveAndFlush(column);

        return columnDtoFactory.makeColumnDto(column);
    }

    public ColumnDto renameColumn(Long boardId, Long columnId, String columnName) {

        if (columnName.isBlank()) {
            throw new BadRequestException("New task state name can`t be null!");
        }

        BoardEntity board = controllerHelper.getBoardOrThrowException(boardId);

        ColumnEntity column = board.getColumns()
                .stream()
                .filter(sameColumn -> Objects.equals(sameColumn.getId(), columnId))
                .findAny()
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Project with \"%s\" id, doesn`t contains task state with \"%s\" id",
                                        boardId, columnId))
                );

        column.setName(columnName);

        columnRepository.saveAndFlush(column);

        return columnDtoFactory.makeColumnDto(column);
    }



    @Override
    public AskDto deleteColumn(Long boardId, Long columnId) {

        BoardEntity board = controllerHelper.getBoardOrThrowException(boardId);

        ColumnEntity column = board.getColumns()
                .stream()
                .filter(sameColumn -> Objects.equals(sameColumn.getId(), columnId))
                .findAny()
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format("Column with \"%s\" id, doesn`t contains task state with \"%s\" id",
                                        boardId, columnId))
                );

        columnRepository.deleteById(column.getId());
        columnRepository.decrementOrdinalsGreaterThan(boardId, column.getOrdinal());

        return AskDto.makeDefault(true);
    }
}
