package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.common.model.BaseEntity;
import com.javarush.jira.common.util.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "task_tag")
@NoArgsConstructor
@Getter
@Setter
public class TaskTag extends BaseEntity {

    @NotNull
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    private Task task;

    @NotBlank
    @Size(min = 2, max = 256)
    @Column(name = "tag", nullable = false)
    @NoHtml
    private String tag;

    public TaskTag(long taskId, String tag) {
        this.taskId = taskId;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TaskTag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                '}';
    }
}

