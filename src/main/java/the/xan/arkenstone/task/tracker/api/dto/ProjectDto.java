package the.xan.arkenstone.task.tracker.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDto {
    Long id;

    String name;

    @JsonProperty("created_at")
    Instant createdAt;

    @JsonProperty("updated_at")
    Instant updatedAt;

}
