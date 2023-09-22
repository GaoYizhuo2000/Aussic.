package au.edu.anu.Aussic.models.userAction;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;




public class Test {
    public static void main(String[] args) throws IOException {
        FileReader fileReader = new FileReader("userActions.json");
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(fileReader, JsonArray.class);
        fileReader.close();

        Random random = new Random();
        int randomIndex = random.nextInt(jsonArray.size());
        JsonObject jsonObject = jsonArray.get(randomIndex).getAsJsonObject();

        UserAction userAction = UserActionFactory.createUserAction(jsonObject);
        System.out.println(userAction.getToastMessage());
    }
}
