package com.fuck_ruangong.utils;

import com.fuck_ruangong.entity.Fraction;
import groovy.lang.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * 工具类：题目生成
 */
public class QuizGenerator {

    private static final String[] OPERATORS = {"+", "-", "×", "÷"};
    private static final Random RANDOM = new Random();

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
     * 随机生成单个题目
     *
     * @param range             操作数的大小范围
     * @param maxOperators      操作符的最大数量
     * @param numberOfQuestions 最大题目数
     * @return 题目，答案
     */
    public static Tuple2<List<String>, List<String>> generateQuiz(int range, int maxOperators, int numberOfQuestions) {
        int duplicateCount = 0;
        int negativeCount = 0;
        int totalCount = 0;

        List<String> quizzes = new ArrayList<>(numberOfQuestions);
        List<String> answers = new ArrayList<>(numberOfQuestions);
        for (int i = 1; i <= numberOfQuestions; i++) {
            while (true) {
                totalCount++;
                List<String> operands = new ArrayList<>();
                List<String> operators = new ArrayList<>();

                for (int j = 0; j < maxOperators + 1; j++) {
                    String lastOperator = operators.isEmpty() ? "" : operators.get(operators.size() - 1);
                    Fraction operand = FractionUtils.generateRandomOperand(lastOperator, range);
                    operands.add(operand.toString());
                    if (j < maxOperators) {
                        operators.add(generateRandomOperator());
                    }
                }

                StringBuilder quiz = new StringBuilder();
                for (int j = 0; j < operands.size(); j++) {
                    quiz.append(operands.get(j));
                    if (j < operators.size()) {
                        quiz.append(" ").append(operators.get(j)).append(" ");
                    }
                }
                String expression = quiz.toString();

                if (operands.size() > 2 && RANDOM.nextBoolean()) {
                    expression = addRandomParentheses(expression);
                }

                String postfix = ExpressionUtils.infixToPostfix(expression);
                Fraction result = ExpressionUtils.evaluatePostfix(postfix);
                if (Objects.nonNull(result)) {
                    if (!DuplicateChecker.isDuplicate(result, expression)) {
                        quizzes.add(i + ". " + expression);
                        answers.add(i + ". " + result);
                        break;
                    } else {
                        duplicateCount++;
                    }
                } else {
                    negativeCount++;
                }
            }
        }
        System.out.println("生成" + numberOfQuestions + "道题目完成，总次数：" + totalCount + "，重复次数：" + duplicateCount + "，负数次数：" + negativeCount);
        return new Tuple2<>(quizzes, answers);
    }

    /**
     * 随机添加括号，只给加法或减法的子表达式加括号
     *
     * @param expression 算数表达式
     * @return 带括号的表达式
     */
    private static String addRandomParentheses(String expression) {
        String[] tokens = expression.split(" ");
        StringBuilder result = new StringBuilder();
        List<Integer> addSubIndices = new ArrayList<>();

        for (int i = 1; i < tokens.length; i += 2) {
            String operator = tokens[i];
            if ("+".equals(operator) || "-".equals(operator)) {
                addSubIndices.add(i);
            }
        }

        if (!addSubIndices.isEmpty()) {
            int index = addSubIndices.get(RANDOM.nextInt(addSubIndices.size()));
            for (int i = 0; i < tokens.length; i++) {
                if (i == index - 1) {
                    result.append("(");
                }
                result.append(tokens[i]);
                if (i == index + 1) {
                    result.append(") ");
                } else if (i < tokens.length - 1) {
                    result.append(" ");
                }
            }
        } else {
            result.append(expression);
        }

        return result.toString();
    }
}
