package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.task.to.TaskToFull;
import com.javarush.jira.bugtracking.task_tag.mapper.TaskTagMapper;
import com.javarush.jira.bugtracking.task_tag.to.TaskTagTo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskTagService {

    TaskTagRepository taskTagRepository;
    TaskTagMapper taskTagMapper;

    public TaskTagTo get(long id) {
        TaskTag taskTag = taskTagRepository.findByIdWithTaskAndProject(id).orElseThrow();
        TaskTagTo taskTagTo = taskTagMapper.toTo(taskTag);
        return taskTagTo;
    }
}
