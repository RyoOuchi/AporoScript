package main.java.com.aporo.codeparser.InstructionClasses;

import main.java.com.aporo.codeparser.CodeParserHelper.CodeParserContext;
import main.java.com.aporo.codeparser.CodeParserHelper.EvaluationHelper;
import main.java.com.aporo.codeparser.CodeParserHelper.Instruction;
import main.java.com.aporo.codeparser.CodeParserHelper.InstructionRegistry;

import java.util.ArrayList;
import java.util.List;

public class WhileInstruction implements Instruction {

    private final String instructionName;

    public WhileInstruction(final String instructionName) {
        this.instructionName = instructionName;
    }
    @Override
    public boolean matches(String[] tokens) {
        // Matches: while x < 3
        return tokens.length == 4 && tokens[0].equals(this.instructionName);
    }

    @Override
    public int execute(String[] lines, int startIndex, CodeParserContext context, InstructionRegistry registry) {
        System.err.println("Executing WhileInstruction at line " + startIndex);

        String[] headerTokens = lines[startIndex].trim().split("\\s+");
        String varName = headerTokens[1];
        String operator = headerTokens[2];
        String value = headerTokens[3];

        int baseIndent = countLeadingSpaces(lines[startIndex]);
        int i = startIndex + 1;

        // Gather the actual line indices of the block
        List<Integer> blockLineIndices = new ArrayList<>();
        // SAFER block parsing – do not include lines at same indentation level
        while (i < lines.length) {
            String line = lines[i];
            int indent = countLeadingSpaces(line);

            if (line.trim().isEmpty()) {
                i++;
                continue;
            }

            if (indent > baseIndent) {
                blockLineIndices.add(i);
            } else {
                break; // Exit block when indentation drops or is equal
            }

            i++;
        }


        System.err.println("Block to loop:");
        for (int idx : blockLineIndices) {
            System.err.println(lines[idx].trim());
        }

        // Repeat while condition holds
        System.err.println(">>> Evaluating while (" + varName + " " + operator + " " + value + ")");

        while (EvaluationHelper.evaluateCondition(varName, operator, value, context)) {
            for (int idx = 0; idx < blockLineIndices.size(); idx++) {
                int lineIndex = blockLineIndices.get(idx);
                String[] tokens = lines[lineIndex].trim().split("\\s+");

                for (Instruction instr : registry.getAllInstructions()) {
                    if (instr.matches(tokens)) {
                        int nextIndex = instr.execute(lines, lineIndex, context, registry);

                        // Skip lines already handled by nested blocks
                        while (idx + 1 < blockLineIndices.size() && blockLineIndices.get(idx + 1) < nextIndex) {
                            idx++;
                        }
                        break;
                    }
                }
            }

        }

        System.err.println("Returning index after while block: " + i);
        return i;
    }

    private int countLeadingSpaces(String line) {
        int count = 0;
        while (count < line.length()) {
            char c = line.charAt(count);
            if (c == ' ') count++;
            else if (c == '\t') count += 4;  // Treat tab as 4 spaces
            else break;
        }
        return count;
    }
}
