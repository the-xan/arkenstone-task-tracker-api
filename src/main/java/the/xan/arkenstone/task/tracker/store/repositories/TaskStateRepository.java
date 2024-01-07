package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;


public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
    @Query("SELECT COALESCE(MAX(t.ordinal), 0) FROM TaskStateEntity t WHERE t.project.id = :projectId") //HQL
    Integer findMaxOrdinalByProjectId(Long projectId);

    // Увеличение порядковых номеров таск стейтов между заданными значениями
    @Modifying
    @Query("UPDATE TaskStateEntity ts SET ts.ordinal = ts.ordinal + 1 WHERE ts.project.id = :projectId AND ts.ordinal >= :newOrdinal AND ts.ordinal < :currentOrdinal")
    void incrementOrdinalsBetween(Long projectId,int currentOrdinal , int newOrdinal);

    // Уменьшение порядковых номеров таск стейтов между заданными значениями
    @Modifying
    @Query("UPDATE TaskStateEntity ts SET ts.ordinal = ts.ordinal - 1 WHERE ts.project.id = :projectId AND ts.ordinal > :currentOrdinal AND ts.ordinal <= :newOrdinal")
    void decrementOrdinalsBetween(Long projectId, int currentOrdinal, int newOrdinal);

    // Уменьшение порядковых номеров таск стейтов после удаления
    @Modifying
    @Query("UPDATE TaskStateEntity ts SET ts.ordinal = ts.ordinal - 1 WHERE ts.project.id = :projectId AND ts.ordinal > :ordinalToDelete")
    void decrementOrdinalsGreaterThan(Long projectId, int ordinalToDelete);
}

