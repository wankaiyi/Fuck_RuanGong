package com.fuck_ruangong.utils;

import com.fuck_ruangong.entity.Fraction;

import java.util.Deque;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * 工具类：表达式相关操作
 */
public class ExpressionUtils {

    /**
     * 将中缀表达式转换为后缀表达式
     */
    public static String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Deque<Character> operatorStack = new LinkedList<>();
        StringTokenizer tokens = new StringTokenizer(infix, "()+-÷×", true);

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.isEmpty()) {
                continue;
            }

            if (isNumber(token)) {
                postfix.append(token).append(' ');
            } else if ("(".equals(token)) {
                operatorStack.push('(');
            } else if (")".equals(token)) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals('(')) {
                    postfix.append(operatorStack.pop()).append(' ');
                }
                operatorStack.pop(); // Remove '('
            } else if (isOperator(token.charAt(0))) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(token.charAt(0))) {
                    postfix.append(operatorStack.pop()).append(' ');
                }
                operatorStack.push(token.charAt(0));
            }
        }
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop()).append(' ');
        }
        return postfix.toString().trim();
    }

    /**
     * 计算运算符优先级
     */
    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '×':
            case '÷':
                return 2;
        }
        return -1;
    }

    /**
     * 判断操作数格式是否满足自然数或真分数的正则表达式
     *
     * @param operator 操作数
     */
    public static boolean isNumber(String operator) {
        String regex = "^\\d+'\\d+/\\d+$|^\\d+/\\d+$|^\\d+$";
        return operator.matches(regex);
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '×' || c == '÷';
    }

    /**
     * 计算后缀表达式， 如果出现负数，返回null
     *
     * @param postfix 后缀表达式
     * @return 计算结果
     */
    public static Fraction evaluatePostfix(String postfix) {
        Deque<Fraction> stack = new LinkedList<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Fraction.parseFraction(token));
            } else if (isOperator(token.charAt(0))) {
                Fraction operand2 = stack.pop();
                Fraction operand1 = stack.pop();
                Fraction result = FractionUtils.calculate(operand1, operand2, token.charAt(0));
                if (result.isNegative()) {
                    // 负数
                    return null;
                }
                stack.push(result);
            }
        }
        return stack.pop();
    }

    public static Fraction evaluatePostfixAllowNegative(String postfix) {
        Deque<Fraction> stack = new LinkedList<>();
        String[] tokens = postfix.split(" ");

        for (String token : tokens) {
            if (isNumber(token)) {
                stack.push(Fraction.parseFraction(token));
            } else if (isOperator(token.charAt(0))) {
                Fraction operand2 = stack.pop();
                Fraction operand1 = stack.pop();
                Fraction result = FractionUtils.calculate(operand1, operand2, token.charAt(0));
                stack.push(result);
            }
        }
        return stack.pop();
    }
}
