package com.fuck_ruangong;

import com.fuck_ruangong.entity.Fraction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String[] OPERATORS = {"+", "-", "×", "÷"};
    private static final Random RANDOM = new Random();

    // 随机生成真分数
    public static Fraction generateRandomFraction(int range) {
        int numerator = RANDOM.nextInt(range) + 1;
        int denominator = RANDOM.nextInt(range) + 1;
        return new Fraction(numerator, denominator);
    }

    // 随机生成自然数或真分数
    public static Fraction generateRandomOperand(int range) {
        if (RANDOM.nextBoolean()) {
            // 真分数
            return generateRandomFraction(range);
        } else {
            // 自然数
            return new Fraction(RANDOM.nextInt(range));
        }
    }

    // 随机生成运算符
    public static String generateRandomOperator() {
        return OPERATORS[RANDOM.nextInt(OPERATORS.length)];
    }

    // 生成题目并计算答案
    public static String generateQuiz(int range) {
        // 生成两个随机操作数
        Fraction operand1 = generateRandomOperand(range);
        Fraction operand2 = generateRandomOperand(range);

        // 生成运算符
        String operator = generateRandomOperator();

        // 计算结果
        Fraction result = calculate(operand1, operand2, operator);

        // 返回题目与答案
        return String.format("%s %s %s = %s", operand1, operator, operand2, result);
    }

    // 进行四则运算
    public static Fraction calculate(Fraction operand1, Fraction operand2, String operator) {
        switch (operator) {
            case "+":
                return operand1.add(operand2);
            case "-":
                return operand1.subtract(operand2);
            case "×":
                return operand1.multiply(operand2);
            case "÷":
                return operand1.divide(operand2);
            default:
                throw new IllegalArgumentException("无效的运算符");
        }
    }

    public static void main(String[] args) {
        // 默认参数
        int numberOfQuestions = 10;
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
        List<String> quizzes = new ArrayList<>();

        for (int i = 0; i < numberOfQuestions; i++) {
            quizzes.add(generateQuiz(range));
        }
        quizzes.forEach(System.out::println);
    }
}
