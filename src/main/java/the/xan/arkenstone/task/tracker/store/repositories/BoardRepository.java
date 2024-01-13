package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import the.xan.arkenstone.task.tracker.store.entities.BoardEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    Optional<BoardEntity> findByName(String name);

    Stream<BoardEntity> streamAllBy();

    Stream<BoardEntity> streamAllByNameStartsWithIgnoreCase(String prefixName);

}
