package the.xan.arkenstone.task.tracker.api.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardDto {
    Long id;

    String name;

    @JsonProperty("created_at")
    Instant createdAt;

    @JsonProperty("updated_at")
    Instant updatedAt;

}
