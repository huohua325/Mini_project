package com.shapeville.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shapeville.model.UserProgress;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class DataManager {
    private static final String DATA_PATH = "src/main/resources/data/user_progress.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // 读取进度
    public static UserProgress loadProgress() {
        File file = new File(DATA_PATH);
        if (!file.exists()) {
            return new UserProgress(); // 返回默认进度
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            return gson.fromJson(reader, UserProgress.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserProgress();
        }
    }

    // 保存进度
    public static void saveProgress(UserProgress progress) {
        File file = new File(DATA_PATH);
        file.getParentFile().mkdirs(); // 确保目录存在
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(progress, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 