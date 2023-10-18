package au.edu.anu.Aussic.models.userAction;

import com.google.gson.JsonObject;

/**
 * @author Yizhuo Gao
 */

public class UserActionFactory {
    public static UserAction createUserAction(JsonObject jsonObject) {
        String actionType = jsonObject.get("actionType").getAsString();
        if ("like".equals(actionType)) {
            return new Like(
                    actionType,
                    jsonObject.get("userName").getAsString(),
                    jsonObject.get("targetSong").getAsString(),
                    jsonObject.get("targetSongId").getAsInt()

            );
        } else if ("comment".equals(actionType)) {
            return new Comment(
                    actionType,
                    jsonObject.get("userName").getAsString(),
                    jsonObject.get("targetSong").getAsString(),
                    jsonObject.get("targetSongId").getAsInt(),
                    jsonObject.get("content").getAsString()

            );
        }
        else if ("favorite".equals(actionType)) {
            return new Favorites(
                    actionType,
                    jsonObject.get("userName").getAsString(),
                    jsonObject.get("targetSong").getAsString(),
                    jsonObject.get("targetSongId").getAsInt()
            );
        }
        return null;
    }
}
