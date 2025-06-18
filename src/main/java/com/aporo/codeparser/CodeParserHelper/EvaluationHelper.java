package main.java.com.aporo.codeparser.CodeParserHelper;

import java.util.Map;

public class EvaluationHelper {
    public static boolean evaluateCondition(String varName, String operator, String value, CodeParserContext context) {
        final Map<String, String> variables = context.getAllVariables();
        String actual = variables.get(varName);
        if (actual == null) return false;
        return switch (operator) {
            case "==" -> actual.equals(value);
            case "!=" -> !actual.equals(value);
            case ">" -> Integer.parseInt(actual) > Integer.parseInt(value);
            case "<" -> Integer.parseInt(actual) < Integer.parseInt(value);
            default -> false;
        };
    }

    public static String evaluateExpression(CodeParserContext context, String... tokens) {
        if (tokens.length == 1) {
            return resolveValue(tokens[0], context); // 値または変数名
        }

        // 左から順に計算（優先順位なし）
        int result = Integer.parseInt(resolveValue(tokens[0], context));
        for (int i = 1; i < tokens.length - 1; i += 2) {
            String op = tokens[i];
            int next = Integer.parseInt(resolveValue(tokens[i + 1], context));

            switch (op) {
                case "+": result += next; break;
                case "-": result -= next; break;
                case "*": result *= next; break;
                case "/": result /= next; break;
                default:
                    throw new IllegalArgumentException("wtf is this operator: " + op);
            }
        }
        return Integer.toString(result);
    }

    private static String resolveValue(String token, CodeParserContext context) {
        final Map<String, String> variables = context.getAllVariables();
        return variables.getOrDefault(token, token);
    }
}
