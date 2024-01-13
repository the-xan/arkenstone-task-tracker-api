package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import the.xan.arkenstone.task.tracker.store.entities.ColumnEntity;

import java.util.stream.Stream;


public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
    @Query("SELECT COALESCE(MAX(t.ordinal), 0) " +
            "FROM ColumnEntity t " +
            "WHERE t.board.id = :boardId") //HQL
    Integer findMaxOrdinalByBoardId(Long boardId);

    // Увеличение порядковых номеров таск стейтов между заданными значениями
    @Modifying
    @Query("UPDATE ColumnEntity ts " +
            "SET ts.ordinal = ts.ordinal + 1 " +
            "WHERE ts.board.id = :boardId " +
            "AND ts.ordinal >= :newOrdinal " +
            "AND ts.ordinal < :currentOrdinal")
    void incrementOrdinalsBetween(Long boardId,int currentOrdinal , int newOrdinal);

    // Уменьшение порядковых номеров таск стейтов между заданными значениями
    @Modifying
    @Query("UPDATE ColumnEntity ts " +
            "SET ts.ordinal = ts.ordinal - 1 " +
            "WHERE ts.board.id = :boardId " +
            "AND ts.ordinal > :currentOrdinal " +
            "AND ts.ordinal <= :newOrdinal")
    void decrementOrdinalsBetween(Long boardId, int currentOrdinal, int newOrdinal);

    // Уменьшение порядковых номеров таск стейтов после удаления
    @Modifying
    @Query("UPDATE ColumnEntity ts " +
            "SET ts.ordinal = ts.ordinal - 1 " +
            "WHERE ts.board.id = :boardId " +
            "AND ts.ordinal > :ordinalToDelete")
    void decrementOrdinalsGreaterThan(Long boardId, int ordinalToDelete);

    Stream<ColumnEntity> streamAllBy();

    Stream<ColumnEntity> streamById(Long taskStateId);




}

