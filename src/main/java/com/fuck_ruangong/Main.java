package com.fuck_ruangong;

import cn.hutool.core.io.FileUtil;
import com.fuck_ruangong.entity.Args;
import com.fuck_ruangong.entity.Fraction;
import com.fuck_ruangong.utils.ExpressionUtils;
import com.fuck_ruangong.utils.FileUtils;
import com.fuck_ruangong.utils.QuizGenerator;
import com.fuck_ruangong.utils.ValidationUtils;
import groovy.lang.Tuple2;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    public static void main(String[] args) {
        // 校验参数
        Args argsObj = ValidationUtils.validateArgs(args);
        int numberOfQuestions = argsObj.getNumberOfQuestions();
        int range = argsObj.getRange();

        // 判题
        checkExercisesAnswers(argsObj.getExercisesFileName(), argsObj.getAnswerFileName());

        System.out.printf("即将生成题目，题目个数：" + numberOfQuestions + "，数据范围：0~%d\n", range - 1);
        // 生成 numberOfQuestions 个题目
        List<Tuple2<String, String>> quizzes = new ArrayList<>();
        for (int i = 0; i < numberOfQuestions; i++) {
            Tuple2<String, String> quizAndAnswer = QuizGenerator.generateQuiz(range - 1, QuizGenerator.generateRandomOperatorCounts());
            quizzes.add(quizAndAnswer);
        }
        String generateExercisesFilePath = System.getProperty("user.dir") + "/Exercises.txt";
        String generateAnswerFilePath = System.getProperty("user.dir") + "/Answers.txt";

        // 删除文件
        File exercisesFile = new File(generateExercisesFilePath);
        File answerFile = new File(generateAnswerFilePath);

        FileUtils.deleteFileIfExists(exercisesFile.getName());
        FileUtils.deleteFileIfExists(answerFile.getName());

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

    /**
     * 校验题目和答案是否匹配，并生成校验结果
     */
    public static void checkExercisesAnswers(String exercisesFileName, String answerFileName) {
        if (StringUtils.isBlank(exercisesFileName) && StringUtils.isBlank(answerFileName)) {
            return;
        }
        String exercisesFilePath = System.getProperty("user.dir") + "/" + exercisesFileName;
        String answerFilePath = System.getProperty("user.dir") + "/" + answerFileName;
        FileUtils.validateFileExists(exercisesFilePath);
        FileUtils.validateFileExists(answerFilePath);
        List<String> exercises = FileUtil.readUtf8Lines(exercisesFilePath);
        List<String> answers = FileUtil.readUtf8Lines(answerFilePath);

        if (exercises.size() != answers.size()) {
            throw new IllegalStateException("题目和答案的数量不一致！");
        }

        System.out.println("开始校验题目和答案...");
        List<String> rightAnswers = new ArrayList<>();
        List<String> wrongAnswers = new ArrayList<>();

        for (int i = 0; i < exercises.size(); i++) {
            String[] parts = exercises.get(i).trim().split("\\.\\s+");
            String exercise = parts[1];
            String answer = answers.get(i).trim().split("\\.\\s+")[1];
            String infixToPostfix = ExpressionUtils.infixToPostfix(exercise);
            Fraction result = ExpressionUtils.evaluatePostfixAllowNegative(infixToPostfix);

            if (StringUtils.equals(result.toString(), answer)) {
                rightAnswers.add(String.valueOf(parts[0]));
            } else {
                wrongAnswers.add(String.valueOf(parts[0]));
            }
        }

        List<String> gradeList = new ArrayList<>();
        gradeList.add("Correct: " + rightAnswers.size() + "(" + StringUtils.join(rightAnswers, ", ") + ")");
        gradeList.add("Wrong: " + wrongAnswers.size() + "(" + StringUtils.join(wrongAnswers, ", ") + ")");
        FileUtil.writeUtf8Lines(gradeList, System.getProperty("user.dir") + "/Grade.txt");

        System.out.println("校验完成，结果已保存至 " + System.getProperty("user.dir") + "/Grade.txt");

    }

}
