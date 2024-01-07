package the.xan.arkenstone.task.tracker.api.services;


import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.ProjectDto;


public interface ProjectService {

    ProjectDto createProject(String name);

    ProjectDto editProject(Long projectId, String name);

    AskDto deleteProject(Long projectId);
}
