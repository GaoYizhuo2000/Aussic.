package au.edu.anu.Aussic.models.parserAndTokenizer;

import java.util.*;

import au.edu.anu.Aussic.models.search.MusicSearchEngine;


public class Parser {
    /**
     * The following exception should be thrown if the parse is faced with series of tokens that do not
     * correlate with any possible production rule.
     */
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }


    private String input;
    private List<Token> tokens;
    //private int position = 0;


    public Parser(String input){
        this.input = input;
        Tokenizer tokenizer = new Tokenizer();
        tokens = tokenizer.tokenize(input);
    }


    //List<Token> tokens = tokenizer.tokenize(input);

    public Map<String, String> tokenToMap() {
        Map<String, String> resultMap = new HashMap<>();
        for (Token token : tokens) {
            resultMap.put(token.getType(), token.getValue());
        }
        return resultMap;
    }

    public void printMap() {
        Map<String, String> tokenMap = tokenToMap();

        for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
            System.out.println("Type1: " + entry.getKey() + ", Value1: " + entry.getValue());
        }
    }


    public Set<String> searchLocal(Map<String, String> tokenMap) {
        MusicSearchEngine engine = new MusicSearchEngine();
        Set<String> resultSet = null;

        for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
            String type = entry.getKey();
            String value = entry.getValue();

            Set<String> currentResult = engine.search(type, value);

            if (resultSet == null) {
                resultSet = currentResult;
            } else {
                resultSet.retainAll(currentResult);  // 取交集
            }
        }

        return resultSet;
    }




}


