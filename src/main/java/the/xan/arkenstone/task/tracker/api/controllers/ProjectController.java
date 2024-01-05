package the.xan.arkenstone.task.tracker.api.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import the.xan.arkenstone.task.tracker.api.model.dto.AskDto;
import the.xan.arkenstone.task.tracker.api.model.dto.ProjectDto;
import the.xan.arkenstone.task.tracker.api.services.ProjectServiceImpl;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@RestController
public class ProjectController {

    private final ProjectServiceImpl projectService;

    private final static String FETCH_PROJECT = "/api/projects";
    private final static String CREATE_PROJECT = "/api/projects";
    private final static String EDIT_PROJECT = "/api/projects/{project_id}";
    private final static String DELETE_PROJECT = "/api/projects/{project_id}";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> fetchProject( // ----> почитать за ResponseEntity<>
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName
    ) {
        return projectService.fetchProject(optionalPrefixName);
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {
        return projectService.createProject(name);
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@PathVariable("project_id") Long projectId,
                                  @RequestParam String name) {
        return projectService.editProject(projectId, name);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AskDto deleteProject(@PathVariable("project_id") Long projectId) {
        return projectService.deleteProject(projectId);
    }
}
