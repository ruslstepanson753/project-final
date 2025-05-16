package com.javarush.jira.bugtracking.task_tag;

import com.javarush.jira.bugtracking.task.Task;
import com.javarush.jira.common.HasId;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.NoHtml;
import com.javarush.jira.profile.internal.model.Profile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
@ToString
@Entity
@Table(name = "task_tag")
@NoArgsConstructor
@Getter
@Setter
public class TaskTag implements HasId {
    @Id
    @NotNull
    @Column(name = "task_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name = "task_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Transient
    private Task task;

    @NotBlank
    @Size(min = 2, max = 256)
    @Column(name = "tag", nullable = false)
    @NoHtml
    private String tag;

    public TaskTag(long id, String tag) {
        this.id = id;
        this.tag = tag;
    }

}

