package com.javarush.jira.bugtracking.task;

import com.javarush.jira.bugtracking.Handlers;
import com.javarush.jira.bugtracking.task.to.ActivityTo;
import com.javarush.jira.bugtracking.task.to.TaskTo;
import com.javarush.jira.common.error.DataConflictException;
import com.javarush.jira.login.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.javarush.jira.bugtracking.task.TaskUtil.getLatestValue;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final TaskRepository taskRepository;

    private final String STATUS_IN_PROGRESS = "in_progress";
    private final String STATUS_READY_FOR_REVIEW = "ready_for_review";
    private final String STATUS_DONE = "done";

    private final Handlers.ActivityHandler handler;
    private final ActivityRepository activityRepository;

    private static void checkBelong(HasAuthorId activity) {
        if (activity.getAuthorId() != AuthUser.authId()) {
            throw new DataConflictException("Activity " + activity.getId() + " doesn't belong to " + AuthUser.get());
        }
    }

    @Transactional
    public Activity create(ActivityTo activityTo) {
        checkBelong(activityTo);
        Task task = taskRepository.getExisted(activityTo.getTaskId());
        if (activityTo.getStatusCode() != null) {
            task.checkAndSetStatusCode(activityTo.getStatusCode());
        }
        if (activityTo.getTypeCode() != null) {
            task.setTypeCode(activityTo.getTypeCode());
        }
        return handler.createFromTo(activityTo);
    }

    @Transactional
    public void update(ActivityTo activityTo, long id) {
        checkBelong(handler.getRepository().getExisted(activityTo.getId()));
        handler.updateFromTo(activityTo, id);
        updateTaskIfRequired(activityTo.getTaskId(), activityTo.getStatusCode(), activityTo.getTypeCode());
    }

    @Transactional
    public void delete(long id) {
        Activity activity = handler.getRepository().getExisted(id);
        checkBelong(activity);
        handler.delete(activity.id());
        updateTaskIfRequired(activity.getTaskId(), activity.getStatusCode(), activity.getTypeCode());
    }

    private void updateTaskIfRequired(long taskId, String activityStatus, String activityType) {
        if (activityStatus != null || activityType != null) {
            Task task = taskRepository.getExisted(taskId);
            List<Activity> activities = handler.getRepository().findAllByTaskIdOrderByUpdatedDesc(task.id());
            if (activityStatus != null) {
                String latestStatus = getLatestValue(activities, Activity::getStatusCode);
                System.out.println();
                if (latestStatus == null) {
                    throw new DataConflictException("Primary activity cannot be delete or update with null values");
                }
                task.setStatusCode(latestStatus);
            }
            if (activityType != null) {
                String latestType = getLatestValue(activities, Activity::getTypeCode);
                if (latestType == null) {
                    throw new DataConflictException("Primary activity cannot be delete or update with null values");
                }
                task.setTypeCode(latestType);
            }
        }
    }

    @Transactional(readOnly = true)
    public Long getDurationOfWorkProcess(TaskTo taskTo) {
        checkTaskToByNull(taskTo);
        Long taskId = taskTo.getId();
        LocalDateTime timeInProcess = activityRepository.findFirstTimeByTaskIdAndByStatus(taskId,STATUS_IN_PROGRESS);
        LocalDateTime timeReadyForReview = activityRepository.findFirstTimeByTaskIdAndByStatus(taskId,STATUS_READY_FOR_REVIEW);

        if ((timeInProcess == null)||(timeReadyForReview == null)) {
            return 0L;
        }

        return Duration.between(timeInProcess, timeReadyForReview).toDays();
    }

    @Transactional(readOnly = true)
    public Long getDurationOfTestProcess(TaskTo taskTo) {
        checkTaskToByNull(taskTo);
        Long taskId = taskTo.getId();
        LocalDateTime timeDone = activityRepository.findFirstTimeByTaskIdAndByStatus(taskId,STATUS_DONE);
        LocalDateTime timeReadyForReview = activityRepository.findFirstTimeByTaskIdAndByStatus(taskId,STATUS_READY_FOR_REVIEW);

        if ((timeDone == null)||(timeReadyForReview == null)) {
            return 0L;
        }

        return Duration.between(timeReadyForReview, timeDone).toDays();
    }

    private static void checkTaskToByNull(TaskTo taskTo) {
        if (taskTo == null || taskTo.getId() == null) {
            throw new IllegalArgumentException("TaskTo or its ID cannot be null");
        }
    }

}
