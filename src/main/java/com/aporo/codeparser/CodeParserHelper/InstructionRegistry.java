package main.java.com.aporo.codeparser.CodeParserHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstructionRegistry {
    private final List<Instruction> instructions = new ArrayList<>();

    public void registerInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    public Optional<Instruction> findInstruction(String[] tokens) {
        return instructions
                .stream()
                .filter(instruction -> instruction.matches(tokens))
                .findFirst();
    }

    public List<Instruction> getAllInstructions() {
        return new ArrayList<>(instructions);
    }
}
