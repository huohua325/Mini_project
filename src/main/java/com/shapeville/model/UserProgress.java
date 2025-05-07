package com.shapeville.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserProgress {
    private int totalScore;
    private Map<String, List<Integer>> taskAttempts; // 任务ID -> 尝试次数列表
    private Map<String, Boolean> taskCompleted;      // 任务ID -> 是否完成
    private Date lastSession;

    public UserProgress() {}

    public UserProgress(int totalScore, Map<String, List<Integer>> taskAttempts, Map<String, Boolean> taskCompleted, Date lastSession) {
        this.totalScore = totalScore;
        this.taskAttempts = taskAttempts;
        this.taskCompleted = taskCompleted;
        this.lastSession = lastSession;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public Map<String, List<Integer>> getTaskAttempts() {
        return taskAttempts;
    }

    public void setTaskAttempts(Map<String, List<Integer>> taskAttempts) {
        this.taskAttempts = taskAttempts;
    }

    public Map<String, Boolean> getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(Map<String, Boolean> taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    public Date getLastSession() {
        return lastSession;
    }

    public void setLastSession(Date lastSession) {
        this.lastSession = lastSession;
    }
}
