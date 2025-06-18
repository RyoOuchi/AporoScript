package main.java.com.aporo.codeparser.InstructionClasses;

import main.java.com.aporo.codeparser.CodeParserHelper.CodeParserContext;
import main.java.com.aporo.codeparser.CodeParserHelper.EvaluationHelper;
import main.java.com.aporo.codeparser.CodeParserHelper.Instruction;
import main.java.com.aporo.codeparser.CodeParserHelper.InstructionRegistry;

import java.util.Arrays;
import java.util.Map;

public class SetInstruction implements Instruction {
    private final String instructionName;

    public SetInstruction(final String instructionName) {
        this.instructionName = instructionName;
    }

    @Override
    public boolean matches(String[] tokens) {
        return tokens.length >= 3 && tokens[0].equals(this.instructionName);
    }

    @Override
    public int execute(String[] lines, int index, CodeParserContext context, InstructionRegistry registry) {
        String[] tokens = lines[index].trim().split("\\s+");
        execute(tokens, context); // delegate to legacy method
        return index + 1;
    }

    private void execute(String[] tokens, CodeParserContext context) {
        final Map<String, String> variables = context.getAllVariables();

        if (tokens.length < 3) {
            throw new IllegalArgumentException("Set instruction requires at least 3 tokens: " + Arrays.toString(tokens));
        }

        final String varName = tokens[1];

        if (!variables.containsKey(varName)) {
            throw new IllegalArgumentException("Variable " + varName + " is not defined.");
        }

        final String[] exprTokens = Arrays.copyOfRange(tokens, 2, tokens.length);
        final String result = EvaluationHelper.evaluateExpression(context, exprTokens);
        variables.put(varName, result);
        System.out.println("Variable updated: " + varName + " = " + result);
    }
}
