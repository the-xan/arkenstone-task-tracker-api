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
@Table(name = "boards")
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    //@Column(unique = true)
    String name;

    @Builder.Default
    Instant updatedAt = Instant.now();

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    @OneToMany(fetch = FetchType.LAZY)
    List<ColumnEntity> columns = new ArrayList<>();

}
