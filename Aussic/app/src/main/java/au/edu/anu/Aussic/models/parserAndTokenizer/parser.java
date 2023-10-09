package au.edu.anu.Aussic.models.parserAndTokenizer;

import java.util.List;
import java.util.ArrayList;



public class Parser {
    private List<Token> tokens;
    private int position = 0;
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public QueryNode parse() {
        QueryNode query = new QueryNode();
        query.conditions = new ArrayList<>();
        while (position < tokens.size()) {
            query.conditions.add(parseCondition());
        }
        return query;
    }

    private ConditionNode parseCondition() {
        System.out.println("Current position: " + position);
        System.out.println("Current token: " + tokens.get(position));

        ConditionNode condition = new ConditionNode();
        Token token = tokens.get(position++);

        condition.type = token.getType();
        condition.value = token.getValue();

        return condition;
    }

}


