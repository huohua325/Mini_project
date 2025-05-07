package com.shapeville.gui.tasks;

public interface TaskPanelInterface {
    void startTask();
    void pauseTask();
    void resumeTask();
    void endTask();
    int getScore();
    String getFeedback();
} 