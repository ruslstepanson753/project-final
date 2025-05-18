package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.task_tag.to.TaskTagTo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.javarush.jira.common.BaseHandler.createdResponse;

@Slf4j
@RestController
@RequestMapping(value = TaskTagController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TaskTagController {

    public static final String REST_URL = "/api/tasks-tags";
    private final Handlers.TaskTagHandler handler;

    @GetMapping("/{id}")
    public TaskTagTo get(@PathVariable long id) {
        log.info("get task-tag by id={}", id);
        return handler
                .getMapper()
                .toTo(
                        handler.getRepository().
                                findByIdWithTaskAndProject(id).
                                orElseThrow(RuntimeException::new)
                );
    }

    @GetMapping()
    public List<TaskTagTo> getAll() {
        log.info("get all task-tag ");
        return handler
                .getMapper()
                .toToList(handler
                        .getRepository()
                                .findAll()
                );
    }


    @GetMapping("/by-task")
    public List<TaskTagTo> getAllByTask(@RequestParam long taskId) {
        log.info("get all tags for task {}", taskId);
        return handler
                .getMapper()
                .toToList(handler
                        .getRepository()
                        .findAllByTaskId(taskId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void create(@Valid @RequestBody TaskTagTo taskTagTo) {
        log.debug("create {}", taskTagTo);
        handler.save(taskTagTo);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void put(@Valid @RequestBody TaskTagTo taskTagTo, @PathVariable long id) {
        log.debug("update {}", taskTagTo);
        handler.putById(taskTagTo,id);
    }

    @PatchMapping("/{id}/change-status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patch(@Valid @RequestBody TaskTagTo taskTagTo, @PathVariable long id) {
        log.debug("update {}", taskTagTo);
        handler.patchById(taskTagTo,id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        handler.delete(id);
    }

}
