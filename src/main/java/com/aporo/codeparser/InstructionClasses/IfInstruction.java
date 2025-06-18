package main.java.com.aporo.codeparser.InstructionClasses;

import main.java.com.aporo.codeparser.CodeParserHelper.CodeParserContext;
import main.java.com.aporo.codeparser.CodeParserHelper.EvaluationHelper;
import main.java.com.aporo.codeparser.CodeParserHelper.Instruction;
import main.java.com.aporo.codeparser.CodeParserHelper.InstructionRegistry;

import java.util.ArrayList;
import java.util.List;

public class IfInstruction implements Instruction {
    private final String instructionName;

    public IfInstruction(String instructionName) {
        this.instructionName = instructionName;
    }
    @Override
    public boolean matches(String[] tokens) {
        // "if x > 10" style line, no braces
        return tokens.length == 4 && tokens[0].equals(this.instructionName);
    }

    @Override
    public int execute(String[] lines, final int startIndex, CodeParserContext context, InstructionRegistry registry) {
        System.err.println("Executing IfInstruction at line " + startIndex);

        // Split the condition line
        String[] headerTokens = lines[startIndex].trim().split("\\s+");
        String varName = headerTokens[1];
        String operator = headerTokens[2];
        String value = headerTokens[3];

        // Get base indentation of the `if` line
        int baseIndent = countLeadingSpaces(lines[startIndex]);
        int i = startIndex + 1;

        List<String> block = new ArrayList<>();
        System.err.println("Parsing block starting from line " + i);

        // Collect indented lines
        while (i < lines.length) {
            String line = lines[i];
            int indent = countLeadingSpaces(line);

            if (line.trim().isEmpty() || indent <= baseIndent) {
                break;  // Block ends when indentation is less or equal
            }

            block.add(line.trim());
            i++;
        }

        // Log the block
        System.err.println("Block to execute:");
        for (String line : block) {
            System.err.println(line);
        }

        // Evaluate the condition
        if (EvaluationHelper.evaluateCondition(varName, operator, value, context)) {
            for (String line : block) {
                String[] tokens = line.split("\\s+");
                for (Instruction instr : registry.getAllInstructions()) {
                    if (instr.matches(tokens)) {
                        instr.execute(new String[]{line}, 0, context, registry);
                        break;
                    }
                }
            }
        }

        System.err.println("Returning index after block: " + i);
        return i;
    }

    private int countLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length() && line.charAt(count) == ' ') {
            count++;
        }
        return count;
    }
}
