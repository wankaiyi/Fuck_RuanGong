package com.fuck_ruangong.utils;

import com.fuck_ruangong.entity.Fraction;

import java.util.Random;

/**
 * 工具类：Fraction 相关操作
 */
public class FractionUtils {

    private static final Random RANDOM = new Random();

    /**
     * 随机生成真分数
     *
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
     *
     * @param operator 操作符
     * @param range    范围
     * @return 随机生成的自然数或真分数
     */
    public static Fraction generateRandomOperand(String operator, int range) {
        if (RANDOM.nextBoolean()) {
            // 真分数
            return generateRandomFraction(range);
        } else {
            // 自然数
            if ("÷".equals(operator)) {
                // 防止被除数为0
                return new Fraction(RANDOM.nextInt(range) + 1);
            }
            return new Fraction(RANDOM.nextInt(range));
        }
    }

    /**
     * 进行四则运算
     *
     * @param operand1 第一个操作数
     * @param operand2 第二个操作数
     * @param operator 运算符
     * @return 运算结果
     */
    public static Fraction calculate(Fraction operand1, Fraction operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1.add(operand2);
            case '-':
                return operand1.subtract(operand2);
            case '×':
                return operand1.multiply(operand2);
            case '÷':
                return operand1.divide(operand2);
            default:
                throw new IllegalArgumentException("无效的运算符");
        }
    }
}
