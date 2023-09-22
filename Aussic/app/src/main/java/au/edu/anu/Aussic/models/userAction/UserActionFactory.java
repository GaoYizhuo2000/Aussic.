package au.edu.anu.Aussic.models.userAction;

import com.google.gson.JsonObject;



public class UserActionFactory {
    public static UserAction createUserAction(JsonObject jsonObject) {
        String actionType = jsonObject.get("actionType").getAsString();
        if ("like".equals(actionType)) {
            return new Like(
                    jsonObject.get("userName").getAsString(),
                    jsonObject.get("targetSong").getAsString(),
                    actionType
            );
        } else if ("comment".equals(actionType)) {
            return new Comment(
                    jsonObject.get("userName").getAsString(),
                    jsonObject.get("targetSong").getAsString(),
                    jsonObject.get("content").getAsString(),
                    actionType
            );
        }

        return null;
    }
}
