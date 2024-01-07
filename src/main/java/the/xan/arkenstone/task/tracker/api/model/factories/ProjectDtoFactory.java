package the.xan.arkenstone.task.tracker.api.model.factories;

import org.springframework.stereotype.Component;
import the.xan.arkenstone.task.tracker.api.model.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.store.entities.ProjectEntity;
@Component
public class ProjectDtoFactory {
    public ProjectDto makeProjectDto(ProjectEntity entity){

        return ProjectDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

    }



}
