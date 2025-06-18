package main.java.com.aporo.codeparser.CodeParserHelper;

import java.util.HashMap;
import java.util.Map;

public class CodeParserContext {
    private final Map<String, String> variables = new HashMap<>();

    public String getVariable(String name) {
        return variables.get(name);
    }

    public void setVariable(String name, String value) {
        variables.put(name, value);
    }

    public Map<String, String> getAllVariables() {
        return variables;
    }
}
