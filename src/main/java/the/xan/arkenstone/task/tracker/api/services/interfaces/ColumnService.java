package the.xan.arkenstone.task.tracker.api.services.interfaces;

import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.ColumnDto;

import java.util.List;
import java.util.Optional;

public interface ColumnService {
    ColumnDto createColumn(Long projectId, String taskStateName);

    List<ColumnDto> getColumns(Long projectId, Optional<Long> optionalTaskStateId);

    ColumnDto updateColumnOrdinal(Long projectId, Long taskStateId, Integer newOrdinal);

    AskDto deleteColumn(Long projectId, Long taskStateId);

}
