package main.java.com.aporo.codeparser.InstructionClasses;

import main.java.com.aporo.codeparser.CodeParserHelper.CodeParserContext;
import main.java.com.aporo.codeparser.CodeParserHelper.Instruction;
import main.java.com.aporo.codeparser.CodeParserHelper.InstructionRegistry;

public class VarInstruction implements Instruction {
    private final String instructionName;

    public VarInstruction(final String instructionName) {
        this.instructionName = instructionName;
    }

    @Override
    public boolean matches(final String[] tokens) {
        return tokens.length >= 3 && tokens[0].equals(this.instructionName);
    }

    @Override
    public int execute(String[] lines, int index, CodeParserContext context, InstructionRegistry registry) {
        String[] tokens = lines[index].trim().split("\\s+");
        execute(tokens, context);
        return index + 1;
    }

    private void execute(final String[] tokens, CodeParserContext context) {
        final String variableName = tokens[1];
        final String variableValue = tokens[2];
        if (variableName.equals("$")) throw new IllegalArgumentException("Variable name cannot be '$'");
        context.setVariable(variableName, variableValue);
        System.out.println("Declared: " + variableName + " = " + variableValue);
    }
}
