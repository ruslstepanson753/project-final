package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.task.Activity;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface TaskTagRepository extends BaseRepository<TaskTag> {
}
