package the.xan.arkenstone.task.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskStateDto {

    @NonNull
    Long id;

    @NonNull
    Long ordinal;

    @NonNull
    String name;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    @NonNull
    List<TaskDto> tasks;


}
