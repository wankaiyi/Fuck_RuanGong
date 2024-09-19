package com.fuck_ruangong.utils;

import com.fuck_ruangong.entity.Fraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工具类：重复性检查
 */
public class DuplicateChecker {

    // key：表达式的结果；value：map(key：表达式除去空格之后的长度，value：map集合（key：表达式中除去空格的每一个字符；value：该字符出现的次数））
    private static final Map<String, Map<Integer, List<Map<Character, Integer>>>> DUMPLICATE_MAP = new HashMap<>();

    public static boolean isDuplicate(Fraction result, String expression) {
        // 已创建的表达式中，如果有计算结果相同，且表达式中的所有字符和出现的次数都一样，就认为是重复的
        String resultStr = result.toString();
        String trimmedExpression = expression.trim();
        Integer length = expression.length();

        // 统计表达式中每个字符出现的次数
        Map<Character, Integer> characterCountMap = trimmedExpression.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue))
                );

        Map<Integer, List<Map<Character, Integer>>> expressionLengthMap = DUMPLICATE_MAP.get(resultStr);

        if (expressionLengthMap != null) {
            // 存在计算结果相同的表达式
            List<Map<Character, Integer>> characterCountMapList = expressionLengthMap.get(length);
            if (characterCountMapList != null) {
                // 存在去除空格后长度相同的表达式
                boolean isDuplicate = characterCountMapList.stream()
                        .anyMatch(map -> map.equals(characterCountMap));

                if (isDuplicate) {
                    return true;
                }
            }
        } else {
            expressionLengthMap = new HashMap<>();
            DUMPLICATE_MAP.put(resultStr, expressionLengthMap);
        }
        List<Map<Character, Integer>> characterCountMapList = expressionLengthMap.computeIfAbsent(length, k -> new ArrayList<>());
        characterCountMapList.add(characterCountMap);
        return false;
    }
}
