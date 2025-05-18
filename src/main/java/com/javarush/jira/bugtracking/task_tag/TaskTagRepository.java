package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.task.Activity;
import com.javarush.jira.common.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskTagRepository extends BaseRepository<TaskTag> {
    @Query("SELECT tt FROM TaskTag tt JOIN FETCH tt.task t JOIN FETCH t.project WHERE tt.id = :id")
    Optional<TaskTag> findByIdWithTaskAndProject(@Param("id") Long id);
}
