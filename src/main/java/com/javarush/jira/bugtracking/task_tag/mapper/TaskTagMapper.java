package com.javarush.jira.bugtracking.task_tag.mapper;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.bugtracking.task_tag.TaskTag;
import com.javarush.jira.bugtracking.task_tag.to.TaskTagTo;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskTagMapper extends BaseMapper<TaskTag, TaskTagTo> {

    @Override
    TaskTagTo toTo(TaskTag taskTag);
}
