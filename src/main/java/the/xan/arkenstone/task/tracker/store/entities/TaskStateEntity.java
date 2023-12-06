package the.xan.arkenstone.task.tracker.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "task_state")
public class TaskStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    private Long ordinal;
    @Column(unique = true)
    String name;

    @Builder.Default
    Instant createdAt = Instant.now();

    @JoinColumn(name = "task_state_id", referencedColumnName = "id")
    @Builder.Default
    @OneToMany
    List<TaskEntity> tasks = new ArrayList<>();

}
