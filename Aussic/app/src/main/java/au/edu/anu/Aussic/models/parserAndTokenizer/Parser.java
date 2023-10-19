package au.edu.anu.Aussic.models.parserAndTokenizer;

import java.util.*;

/**
 * @author: u7581818, Oscar Wei
 * @author: u7552399, Yizhuo Gao
 * @author: u7516507, Evan Cheung
 */

// <Exp> = <term>| <term>;<Exp>
// <Term> = \a<Factor> | \n<Factor> | \g<Factor> | \r<Factor>
// <Factor> = String
// eg: \a Troye; \nStrawberries and cigarettes

public class Parser {
    private Tokenizer tokenizer;
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }
    public Map<String, String> Parse() {
        Map<String, String> searchingTerms = new HashMap<>();
        while(tokenizer.hasNext()){
            if(tokenizer.current().getType() == Token.Type.ARTISTNAME){
                tokenizer.next();
                if(tokenizer.hasNext()){
                    String value = tokenizer.current().getToken();
                    searchingTerms.put("artistName", value);
                    tokenizer.next();
                }else{
                    break;
                }
            }

            else if(tokenizer.current().getType() == Token.Type.SONGNAME) {
                tokenizer.next();
                if(tokenizer.hasNext()){
                    String value = tokenizer.current().getToken();
                    searchingTerms.put("name", value);
                    tokenizer.next();
                }else{
                    break;
                }
            }

            else if(tokenizer.current().getType() == Token.Type.GENRE) {
                tokenizer.next();
                if(tokenizer.hasNext()){
                    String value = tokenizer.current().getToken();
                    searchingTerms.put("genre", value);
                    tokenizer.next();
                }else{
                    break;
                }
            }

            else if(tokenizer.current().getType() == Token.Type.RELEASEDATE) {
                tokenizer.next();
                if(tokenizer.hasNext()){
                    String value = tokenizer.current().getToken();
                    searchingTerms.put("releaseDate", value);
                    tokenizer.next();
                }else{
                    break;
                }
            }
            else if(tokenizer.current().getType() == Token.Type.USER){
                tokenizer.next();
                if(tokenizer.hasNext()){
                    String value = tokenizer.current().getToken();
                    searchingTerms.put("user", value);
                    tokenizer.next();
                }else{
                    break;
                }
            }
            else if(tokenizer.current().getType() == Token.Type.SEMICOLON){
                tokenizer.next();
                if(!tokenizer.hasNext()){break;}
            }
            else if(tokenizer.current().getType() == Token.Type.STRING) {
                searchingTerms.put("undefinedTerm", tokenizer.current().getToken());
                tokenizer.next();
            }
            else{
                break;
            }
        }
        return searchingTerms;
    }
}


