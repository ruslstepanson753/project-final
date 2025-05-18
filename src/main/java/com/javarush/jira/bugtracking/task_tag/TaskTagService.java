package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.task_tag.mapper.TaskTagMapper;
import com.javarush.jira.bugtracking.task_tag.to.TaskTagTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskTagService {

    private final TaskTagRepository taskTagRepository;
    private final TaskTagMapper taskTagMapper;

    public TaskTagService(TaskTagRepository taskTagRepository, TaskTagMapper taskTagMapper) {
        this.taskTagRepository = taskTagRepository;
        this.taskTagMapper = taskTagMapper;
    }

    @Transactional
    public TaskTagTo get(long id) {
        TaskTag taskTag = taskTagRepository.findByIdWithTaskAndProject(id).orElseThrow();
        TaskTagTo taskTagTo = taskTagMapper.toTo(taskTag);
        return taskTagTo;
    }
}
