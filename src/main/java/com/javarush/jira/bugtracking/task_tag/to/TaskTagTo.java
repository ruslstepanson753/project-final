package com.javarush.jira.bugtracking.task_tag.to;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javarush.jira.common.to.BaseTo;
import com.javarush.jira.common.util.validation.Code;
import com.javarush.jira.common.util.validation.NoHtml;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class TaskTagTo extends BaseTo {
    @NotNull
    Long taskId;
    @Size(min = 2, max = 256)
    @Column(name = "tag", nullable = false)
    @NoHtml
    private String tag;
}
