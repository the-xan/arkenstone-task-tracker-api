package the.xan.arkenstone.task.tracker.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.ColumnDto;
import the.xan.arkenstone.task.tracker.api.services.ColumnServiceImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ColumnController {

    private final ColumnServiceImpl columnService;

    private final static String GET_COLUMNS = "/api/boards/{board_id}/columns";
    private final static String CREATE_COLUMN = "/api/boards/{board_id}/columns";
    private final static String EDIT_COLUMN_ORDINAL = "/api/boards/{board_id}/columns/{column_id}/ordinal";
    private final static String DELETE_COLUMN = "/api/boards/{board_id}/columns/{column_id}";
    private final static String EDIT_COLUMN_NAME = "/api/boards/{board_id}/columns/{column_id}/name";

    @GetMapping(GET_COLUMNS)
    public List<ColumnDto> getTaskStates(@PathVariable(name = "board_id") Long boardId,
                                         @RequestParam(name = "column_id", required = false) Optional<Long> optionalTaskStateId) {
        return columnService
                .getColumns(boardId, optionalTaskStateId);
    }

    @PostMapping(CREATE_COLUMN)
    public ColumnDto createTaskState(@PathVariable(value = "board_id") Long boardId,
                                     @RequestParam(name = "column_name") String columnName) {
        return columnService
                .createColumn(boardId, columnName);
    }

    @Transactional
    @PatchMapping (EDIT_COLUMN_ORDINAL)
    public ColumnDto updateTaskStateOrdinal(@PathVariable(value = "board_id") Long boardId,
                                            @PathVariable(value = "column_id") Long columnId,
                                            @RequestParam(name = "column_ordinal") Integer newOrdinal) {
        return columnService
                .updateColumnOrdinal(boardId, columnId, newOrdinal);
    }


    @PatchMapping (EDIT_COLUMN_NAME)
    public ColumnDto updateColumnName(@PathVariable(value = "board_id") Long boardId,
                                      @PathVariable(value = "column_id") Long columnId,
                                      @RequestParam(name = "column_name") String columnName) {
        return columnService
                .renameColumn(boardId,columnId, columnName);
    }


    @Transactional
    @DeleteMapping (DELETE_COLUMN)
    public AskDto deleteTaskState(@PathVariable(value = "board_id") Long boardId,
                                 @PathVariable(value = "column_id") Long columnId) {
        return columnService
                .deleteColumn(boardId, columnId);
    }
}
