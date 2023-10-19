package au.edu.anu.Aussic.models.userAction;

import com.google.gson.JsonObject;

/**
 * Factory class responsible for creating instances of UserAction subclasses based on JSON data.
 * This class allows the creation of Like, Comment, and Favorites user actions from JSON objects.
 *
 * @author u7552399, Yizhuo Gao
 */
public class UserActionFactory {

    /**
     * Creates a specific UserAction instance based on the provided JSON object.
     *
     * @param jsonObject A JSON object representing a user action.
     * @return A UserAction instance (Like, Comment, or Favorites) corresponding to the JSON data.
     *         Returns null if the action type is not recognized.
     */
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
