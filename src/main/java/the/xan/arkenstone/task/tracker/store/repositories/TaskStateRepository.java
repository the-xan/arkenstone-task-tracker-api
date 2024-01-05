package the.xan.arkenstone.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import the.xan.arkenstone.task.tracker.store.entities.TaskStateEntity;


public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {
    @Query("SELECT COALESCE(MAX(t.ordinal), 0) FROM TaskStateEntity t WHERE t.project.id = :projectId") //HQL
    Integer findMaxOrdinalByProjectId(Long projectId);

    @Modifying
    @Query("UPDATE TaskStateEntity t" +
            " SET t.ordinal = t.ordinal + 1" +
            " WHERE t.project.id = :projectId AND t.ordinal >= :newOrdinal AND t.ordinal < :currentOrdinal")
    void incrementOrdinalsBetween(Long projectId, int newOrdinal, int currentOrdinal);

    @Modifying
    @Query("UPDATE TaskStateEntity t" +
            " SET t.ordinal = t.ordinal - 1" +
            " WHERE t.project.id = :projectId AND t.ordinal > :currentOrdinal AND t.ordinal <= :newOrdinal")
    void decrementOrdinalsBetween(Long projectId, int newOrdinal, int currentOrdinal);
}
