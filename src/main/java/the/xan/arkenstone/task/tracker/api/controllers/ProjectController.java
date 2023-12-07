package the.xan.arkenstone.task.tracker.api.controllers;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RestController;
import the.xan.arkenstone.task.tracker.api.factories.ProjectDtoFactory;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectDtoFactory projectDtoFactory;

}
