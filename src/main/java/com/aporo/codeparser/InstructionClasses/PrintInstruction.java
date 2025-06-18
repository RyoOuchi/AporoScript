package main.java.com.aporo.codeparser.InstructionClasses;

import main.java.com.aporo.codeparser.CodeParserHelper.CodeParserContext;
import main.java.com.aporo.codeparser.CodeParserHelper.Instruction;
import main.java.com.aporo.codeparser.CodeParserHelper.InstructionRegistry;

public class PrintInstruction implements Instruction {
    private final String instructionName;

    public PrintInstruction(final String instructionName) {
        this.instructionName = instructionName;
    }

    @Override
    public boolean matches(String[] tokens) {
        return tokens.length >= 2 && tokens[0].equals(instructionName);
    }

    @Override
    public int execute(String[] lines, int index, CodeParserContext context, InstructionRegistry registry) {
        String[] tokens = lines[index].trim().split("\\s+");
        execute(tokens, context); // delegate to legacy method
        return index + 1;
    }

    private void execute(String[] tokens, CodeParserContext context) {
        if (tokens[1].equals("$")) {
            StringBuilder output = new StringBuilder();
            for (int i = 2; i < tokens.length; i++) {
                output.append(tokens[i]);
                if (i < tokens.length - 1) {
                    output.append(" ");
                }
            }
            System.out.println(output);
            return;
        }
        printVariable(tokens, context);
    }

    private void printVariable(final String[] tokens, final CodeParserContext context) {
        final String variableName = tokens[1];
        final String variableValue = context.getVariable(variableName);
        if (variableValue != null) {
            System.out.println(variableValue);
        } else {
            System.out.println("Variable " + variableName + " is not defined.");
        }
    }
}
