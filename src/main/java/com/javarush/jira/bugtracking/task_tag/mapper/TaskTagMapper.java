package com.javarush.jira.bugtracking.task_tag.mapper;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.bugtracking.task_tag.TaskTag;
import com.javarush.jira.bugtracking.task_tag.to.TaskTagTo;
import com.javarush.jira.common.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskTagMapper extends BaseMapper<TaskTag, TaskTagTo> {

    @Override
    @Mapping(target = "id", ignore = true)
    TaskTagTo toTo(TaskTag taskTag);

    @Override
    @Mapping(target = "id", ignore = true) // Если id должен генерироваться автоматически
    TaskTag toEntity(TaskTagTo taskTagTo);
}
