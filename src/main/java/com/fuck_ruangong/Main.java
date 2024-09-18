package com.fuck_ruangong;

import com.fuck_ruangong.entity.Fraction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    private static final String[] OPERATORS = {"+", "-", "×", "÷"};
    private static final Random RANDOM = new Random();

    /**
     * 随机生成真分数
     * @param range 范围
     * @return 随机生成的真分数
     */
    public static Fraction generateRandomFraction(int range) {
        int numerator = RANDOM.nextInt(range) + 1;
        int denominator = RANDOM.nextInt(range) + 1;
        return new Fraction(numerator, denominator);
    }

    /**
     * 随机生成自然数或真分数
     * @param range 范围
     * @return 随机生成的自然数或真分数
     */
    public static Fraction generateRandomOperand(int range) {
        if (RANDOM.nextBoolean()) {
            // 真分数
            return generateRandomFraction(range);
        } else {
            // 自然数
            return new Fraction(RANDOM.nextInt(range));
        }
    }

    /**
     * 随机生成运算符
     */
    public static String generateRandomOperator() {
        return OPERATORS[RANDOM.nextInt(OPERATORS.length)];
    }


    /**
     * 随机生成运算符数量
     */
    public static int generateRandomOperatorCounts() {
        return RANDOM.nextInt(3) + 1;
    }

    /**
     * 生成题目
     * @param range 操作数的大小范围
     * @param maxOperators 操作符的最大数量
     * @return 算数表达式
     */
    public static String generateQuiz(int range, int maxOperators) {
        List<String> operands = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        // 随机生成操作数和运算符
        for (int i = 0; i < maxOperators + 1; i++) {
            operands.add(generateRandomOperand(range).toString());
            if (i < maxOperators) {
                operators.add(generateRandomOperator());
            }
        }

        StringBuilder quiz = new StringBuilder();
        for (int i = 0; i < operands.size(); i++) {
            quiz.append(operands.get(i));
            if (i < operators.size()) {
                quiz.append(" ").append(operators.get(i)).append(" ");
            }
        }
        String expression = quiz.toString();

        // 随机决定是否包含括号
        if (RANDOM.nextBoolean()) {
            expression = addRandomParentheses(expression);
        }
        return expression;
    }

    /**
     * 随机添加括号，只给加法或减法的子表达式加括号
     * @param expression 算数表达式
     * @return 带括号的表达式
     */
    private static String addRandomParentheses(String expression) {
        String[] tokens = expression.split(" ");
        StringBuilder result = new StringBuilder();
        List<Integer> addSubIndices = new ArrayList<>();

        // 收集所有加法和减法运算符的位置
        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            if ("+".equals(operator) || "-".equals(operator)) {
                addSubIndices.add(i);
            }
        }

        // 随机选择一个加法或减法的位置进行括号插入
        if (!addSubIndices.isEmpty()) {
            int index = addSubIndices.get(RANDOM.nextInt(addSubIndices.size()));
            for (int i = 0; i < tokens.length; i++) {
                if (i == index - 1) {
                    result.append("(");
                }
                result.append(tokens[i]);
                if (i == index + 1) {
                    result.append(")");
                } else if (i < tokens.length - 1) {
                    result.append(" ");
                }
            }
        } else {
            // 如果没有加法或减法运算符，返回原始表达式
            result.append(expression);
        }

        return result.toString();
    }

    /**
     * 进行四则运算
     *
     * @param operand1 第一个操作数
     * @param operand2 第二个操作数
     * @param operator 运算符
     * @return 运算结果
     */
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
            quizzes.add(generateQuiz(range, generateRandomOperatorCounts()));
        }
        quizzes.forEach(System.out::println);
    }
}
