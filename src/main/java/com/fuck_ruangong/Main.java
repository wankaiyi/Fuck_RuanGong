package com.fuck_ruangong;

import cn.hutool.core.io.FileUtil;
import com.fuck_ruangong.utils.QuizGenerator;
import groovy.lang.Tuple2;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    private static final String TXT_FILE_PATTERN = "^[^\\s]+\\.txt$";

    private static boolean isValidTxtFile(String filePath) {
        return TXT_FILE_PATTERN.matches(filePath);
    }

    public static void main(String[] args) {
        // 默认10个题目
        int numberOfQuestions = 10;
        // 默认范围，10以内（不包括10）
        int range = 10;

        // 从命令行参数读取 -n 和 -r 参数
        for (int i = 0; i < args.length; i++) {
            if (StringUtils.equals("-n", args[i]) && i + 1 < args.length) {
                try {
                    numberOfQuestions = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("题目个数参数不合法：" + args[i + 1]);
                    return;
                }
            } else if ("-r".equals(args[i]) && i + 1 < args.length) {
                try {
                    range = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.out.println("数据范围参数不合法：" + args[i + 1]);
                    return;
                }
            }
        }
        // 生成10个题目
        System.out.printf("题目个数：" + numberOfQuestions + "，数据范围：0~%d\n", range - 1);
        // 生成 numberOfQuestions 个题目
        List<Tuple2<String, String>> quizzes = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++) {
            Tuple2<String, String> quizAndAnswer = QuizGenerator.generateQuiz(range - 1, QuizGenerator.generateRandomOperatorCounts());
            quizzes.add(quizAndAnswer);
        }
        String generateExercisesFilePath = System.getProperty("user.dir") + "/Exercises.txt";
        String generateAnswerFilePath = System.getProperty("user.dir") + "/Answers.txt";

        // 删除文件如果它们存在
        File exercisesFile = new File(generateExercisesFilePath);
        File answerFile = new File(generateAnswerFilePath);

        if (exercisesFile.exists() && exercisesFile.delete()) {
            System.out.println("删除Exercises.txt");
        }
        if (answerFile.exists() && answerFile.delete()) {
            System.out.println("删除Answers.txt");
        }

        // 初始化计数器
        AtomicReference<Integer> count = new AtomicReference<>(1);

        quizzes.forEach(tuple2 -> {
            String quiz = tuple2.getFirst();
            String answer = tuple2.getSecond();

            // 使用 appendString 方法追加内容
            FileUtil.appendUtf8String(count.get() + ". " + quiz + "\n", exercisesFile);
            FileUtil.appendUtf8String(count.get() + ". " + answer + "\n", answerFile);

            count.getAndSet(count.get() + 1);
        });
    }
}
