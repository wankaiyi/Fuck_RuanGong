package com.fuck_ruangong;

import com.fuck_ruangong.utils.QuizGenerator;
import org.apache.commons.lang3.StringUtils;

public class Main {

    public static void main(String[] args) {
        // 默认参数，10以内（不包括10）
        int numberOfQuestions = 9;
        int range = 9;

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
        for (int i = 0; i < numberOfQuestions; i++) {
            QuizGenerator.generateQuiz(range, QuizGenerator.generateRandomOperatorCounts());
        }
    }
}
