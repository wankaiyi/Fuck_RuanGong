package com.fuckruangong.utils;

import com.fuckruangong.entity.Fraction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工具类：重复性检查
 */
public class DuplicateChecker {

    // key：表达式的结果；value：map(key：表达式的长度，value：map集合（key：表达式中的每一个操作数或操作符；value：该字符串出现的次数））
    private static final Map<String, Map<Integer, List<Map<String, Integer>>>> DUMPLICATE_MAP = new HashMap<>();

    public static boolean isDuplicate(Fraction result, String expression) {
        // 已创建的表达式中，如果有计算结果相同，且表达式中的所有字符和出现的次数都一样，就认为是重复的
        String resultStr = result.toString();
        expression = expression.replaceAll("[()]", "");
        Integer length = expression.length();

        // 统计表达式中每个操作数和操作符出现的次数
        Map<String, Integer> characterCountMap = Arrays.stream(expression.split("\\s+"))
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue))
                );

        Map<Integer, List<Map<String, Integer>>> expressionLengthMap = DUMPLICATE_MAP.get(resultStr);

        if (expressionLengthMap != null) {
            // 存在计算结果相同的表达式
            List<Map<String, Integer>> characterCountMapList = expressionLengthMap.get(length);
            if (characterCountMapList != null) {
                // 存在长度相同的表达式
                boolean isDuplicate = characterCountMapList.stream()
                        .anyMatch(map -> map.equals(characterCountMap));

                if (isDuplicate) {
                    // 存在操作数和操作符出现次数相同的表达式
                    return true;
                }
            }
        } else {
            expressionLengthMap = new HashMap<>();
            DUMPLICATE_MAP.put(resultStr, expressionLengthMap);
        }
        List<Map<String, Integer>> characterCountMapList = expressionLengthMap.computeIfAbsent(length, k -> new ArrayList<>());
        characterCountMapList.add(characterCountMap);
        return false;
    }
}
