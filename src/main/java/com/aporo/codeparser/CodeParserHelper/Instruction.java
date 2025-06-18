package main.java.com.aporo.codeparser.CodeParserHelper;

public interface Instruction {
    boolean matches(String[] tokens);

    int execute(String[] lines, int startIndex, CodeParserContext context, InstructionRegistry registry);
}
