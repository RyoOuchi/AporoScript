package main.java.com.aporo.codeparser;

import main.java.com.aporo.codeparser.CodeParserHelper.CodeParserContext;
import main.java.com.aporo.codeparser.CodeParserHelper.Instruction;
import main.java.com.aporo.codeparser.CodeParserHelper.InstructionRegistry;
import main.java.com.aporo.codeparser.InstructionClasses.*;

import java.util.Optional;

public class CodeParser {
    private final String code;
    private final InstructionRegistry registry = new InstructionRegistry();
    private final CodeParserContext context = new CodeParserContext();

    public CodeParser(String code) {
        if (code == null) throw new IllegalArgumentException("Code cannot be null");
        if (code.isEmpty()) throw new IllegalArgumentException("Code cannot be empty");

        this.code = code;
        this.registerInstructions();
    }

    private void registerInstructions() {
        registry.registerInstruction(new VarInstruction("var"));
        registry.registerInstruction(new PrintInstruction("print"));
        registry.registerInstruction(new SetInstruction("set"));
        registry.registerInstruction(new WhileInstruction("while"));
        registry.registerInstruction(new IfInstruction("if"));
    }

    public void registerInstruction(Instruction instruction) {
        registry.registerInstruction(instruction);
    }

    public void parseCode() {
        String[] lines = code.split("\n");
        int i = 0;
        while (i < lines.length) {
            String line = lines[i].trim();
            if (line.isEmpty() || line.equals("}")) {
                i++;
                continue;
            }

            String[] tokens = line.split("\\s+");

            Optional<Instruction> matched = registry.findInstruction(tokens);
            if (matched.isPresent()) {
                Instruction instr = matched.get();
                System.err.println("Executing instruction at line " + i); // Debug log
                i = instr.execute(lines, i, context, registry); // Update the index after execution
                System.err.println("New index after execution: " + i); // Debug log
            } else {
                System.out.println("Unknown instruction: " + line);
                i++;
            }
        }
    }
}
