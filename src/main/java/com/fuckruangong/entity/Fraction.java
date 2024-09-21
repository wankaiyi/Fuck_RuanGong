package com.fuckruangong.entity;

public class Fraction {
    /**
     * 分子
     */
    private int numerator;

    /**
     * 分母
     */
    private int denominator;

    public Fraction(int numerator, int denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("分母不能为0");
        }
        this.numerator = numerator;
        this.denominator = denominator;
        simplify(); // 初始化时约分
    }

    public Fraction(int numerator) {
        this(numerator, 1);
    }

    // 计算最大公约数
    private int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // 约分
    private void simplify() {
        int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        numerator /= gcd;
        denominator /= gcd;
        if (denominator < 0) {
            // 确保分母为正
            numerator = -numerator;
            denominator = -denominator;
        }
    }

    // 倒数
    public Fraction reciprocal() {
        if (numerator == 0) {
            return this;
        }
        return new Fraction(denominator, numerator);
    }

    // 加法运算，先通分再计算
    public Fraction add(Fraction other) {
        int commonDenominator = this.denominator * other.denominator;
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        return new Fraction(newNumerator, commonDenominator);
    }

    // 减法运算，先通分再计算
    public Fraction subtract(Fraction other) {
        int commonDenominator = this.denominator * other.denominator;
        int newNumerator = this.numerator * other.denominator - other.numerator * this.denominator;
        return new Fraction(newNumerator, commonDenominator);
    }

    // 乘法运算
    public Fraction multiply(Fraction other) {
        return new Fraction(this.numerator * other.numerator, this.denominator * other.denominator);
    }

    // 除法运算
    public Fraction divide(Fraction other) {
        return this.multiply(other.reciprocal());
    }

    public static Fraction parseFraction(String fractionStr) {
        if (fractionStr.contains("'")) {
            String[] parts = fractionStr.split("'");
            int wholePart = Integer.parseInt(parts[0]);
            String fractionalPart = parts[1];
            return parseFraction(fractionalPart).add(new Fraction(wholePart));
        }

        if (fractionStr.contains("/")) {
            String[] parts = fractionStr.split("/");
            int numerator = Integer.parseInt(parts[0]);
            int denominator = Integer.parseInt(parts[1]);
            return new Fraction(numerator, denominator);
        }

        return new Fraction(Integer.parseInt(fractionStr));
    }


    @Override
    public String toString() {
        // 如果分母为1，直接返回分子
        if (denominator == 1) {
            return String.valueOf(numerator);
        }

        if (Math.abs(numerator) > denominator) {
            // 整数部分
            int wholePart = numerator / denominator;
            // 余数部分
            int remainder = Math.abs(numerator % denominator);

            // 如果没有余数，直接返回整数部分
            if (remainder == 0) {
                return String.valueOf(wholePart);
            }
            return String.format("%d'%d/%d", wholePart, remainder, denominator);
        }
        return numerator + "/" + denominator;
    }

    public boolean isNegative() {
        return numerator < 0;
    }
}

