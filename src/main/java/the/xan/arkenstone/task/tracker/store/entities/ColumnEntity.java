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
@Table(name = "columns")
public class ColumnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    int ordinal;

    String name;

    @Builder.Default
    Instant createdAt = Instant.now();

    @ManyToOne
    BoardEntity board;

    @JoinColumn(name = "column_id", referencedColumnName = "id")
    @Builder.Default
    @OneToMany
    List<TaskEntity> tasks = new ArrayList<>();

}
