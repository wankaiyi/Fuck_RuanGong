# 结对项目：四则运算题目生成
| 这个作业属于哪个课程 | [22级计科1班](https://edu.cnblogs.com/campus/gdgy/CSGrade22-12) |
| -------------------- | ------------------------------------------------------------ |
| 这个作业要求在哪里   | [作业要求](https://edu.cnblogs.com/campus/gdgy/CSGrade22-12/homework/13221) |
| 这个作业的目标       | 自动生成小学四则运算题目             |
| github项目链接       | [链接](https://github.com/wankaiyi/Fuck_RuanGong)                 |

# 姓名&学号
| 姓名 | 学号 |
| -----| --- |
| 万凯毅 | 3122004788|
| 周彦安 | 3122004804 |

# PSP

| PSP2.1                                  | Personal Software Process Stages | 预估耗时（分钟） | 实际耗时（分钟） |
|-----------------------------------------|----------------------------------|----------|----------|
| **Planning**                            | 计划                               | 30 | 30 |
| · Estimate                              | 估计这个任务需要多少时间                     | 30 | 30 |
| **Development**                         | 开发                               | 1180 | 1180 |
| · Analysis                              | 需求分析（包括学习新技术）                    | 60 | 60 |
| · Design Spec                           | 生成设计文档                           | 60 | 60 |
| · Design Review                         | 设计复审                             | 60 | 60 |
| · Coding Standard                       | 代码规范（为当前的开发制定合适的规范）              | 60 | 60 |
| · Design                                | 具体设计                             | 360 | 360 |
| · Coding                                | 具体编码                             | 360 | 360 |
| · Code Review                           | 代码复审                             | 100 | 100 |
| · Test                                  | 测试（自我测试、修改代码、提交修改）               | 120 | 120 |
| **Reporting**                           | 报告                               | 160 | 160 |
| · Test Report                           | 测试报告                             | 100 | 100 |
| · Size Measurement                      | 计算工作量                            | 30 | 30 |
| · Postmortem & Process Improvement Plan | 事后总结，并提出过程改进计划                   | 30 | 30 |
| **Total**                               | 合计                               | 1370 | 1370 |

# 功能
## 生成题目
### 输出
#### 控制台输出
![image](https://github.com/user-attachments/assets/7a3d438c-3244-4d99-b075-2a2487981d94)

#### 题目
![image](https://github.com/user-attachments/assets/1951205e-7299-4d1f-8205-3eb049197971)

#### 答案
![image](https://github.com/user-attachments/assets/83f701cc-4502-4931-88a3-7e4ae5174e38)

### 效能分析
#### 优化前
每生成一个题目就使用append的形式写入文件

![image](https://github.com/user-attachments/assets/804307b3-3c16-42b3-baee-88bce9c8b693)

#### 优化后
生成所有题目后，一次性写入文件

<img width="715" alt="e39619ee961bfa8885385a9ebf760aa" src="https://github.com/user-attachments/assets/adabe142-26c0-459a-831b-133d3c96dcd1">

#### 分析
优化前后生成10000道题目以及答案的耗时都是700ms左右，性能差别主要在文件io上，优化前相当于对文件做了10000次io，优化后只有一次io

### 设计实现过程
#### 总体流程
![image](https://github.com/user-attachments/assets/5169ed07-1ee2-4b21-a402-427046d05685)

#### 生成单个题目流程
![image](https://github.com/user-attachments/assets/252d5359-dee5-4588-a547-cc4af7b3f42a)

#### 判断题目是否重复
利用多级树型结构逐层判断，如果到最后一层出现相同的，则认为是重复表达式：

![image](https://github.com/user-attachments/assets/45daab13-bb2e-477d-85f0-afe2f97f2c4c)

### 代码说明
#### 批量随机生成题目
每次生成题目时校验是不是负数、是否重复，如果不满足要求就不断重新生成
```
  /**
     * 批量随机生成题目
     *
     * @param range             操作数的大小范围
     * @param numberOfQuestions 最大题目数
     * @return 题目，答案
     */
    public static Tuple2<List<String>, List<String>> generateQuiz(int range, int numberOfQuestions) {
        int duplicateCount = 0;
        int negativeCount = 0;
        int totalCount = 0;

        List<String> quizzes = new ArrayList<>(numberOfQuestions);
        List<String> answers = new ArrayList<>(numberOfQuestions);
        for (int i = 1; i <= numberOfQuestions; i++) {
            int maxOperators = generateRandomOperatorCounts();
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
```
### 题目去重
```
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
```

## 判题
### 输出
#### 控制台输出
![image](https://github.com/user-attachments/assets/b001a017-9024-494a-bcb7-be00263e72f7)

#### 结果输出
![image](https://github.com/user-attachments/assets/f8bda315-19a8-472b-997c-99e902c46763)

### 效能分析
![image](https://github.com/user-attachments/assets/00b5ad03-4269-46f6-b318-b78c90ec6aa5)

分析：对10000道题目和答案进行校验，耗时主要在题目结果的计算上

# 测试和异常情况
1. args参数：
   - -n 和 -r 的参数如果没有就是默认值10
   - -n 和 -r 的参数必须是整数类型的值
   - -e 和 -a 的参数必须同时存在或不存在
   - -e 和 -a 参数不满足格式 xxx.txt
   - -e 和 -a 参数的文件必须存在
3. 校验题目：题目数和答案数必须一致
4. 生成题目：-r 给出的参数不支持生成 -n 的题目数

# 项目小结
本项目开发了一个智能数学题目生成器，能够随机生成包含加法、减法、乘法和除法的数学表达式，并支持题目的有效性校验与答案计算。加强了代码规范，性能优化，设计分析以及团队合作的能力。
