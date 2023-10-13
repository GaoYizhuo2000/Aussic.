package au.edu.anu.Aussic.models.entity;


import java.util.ArrayList;
import java.util.List;


//用户注册后新建user上传firestore
//登陆之后把用户id传给homeactivity，实例化个人主页fragment的时候再传，然后在fragment初始化的时候根据用户id从firebastore获取用户信息展示出来
public class User {
    public String username;
    public List<String> favorites = new ArrayList<>(); //放歌曲id
    public List<String> likes = new ArrayList<>();
    String location = "";
    public String iconUrl;

    public User(String username, String iconUrl) {
        this.username = username;
        this.iconUrl = iconUrl;
    }

    public void update(){};  //更新数据库里的user数据

    public void addFavorites(String songID){ this.favorites.add(songID); }
    public void addLikes(String songID){ this.likes.add(songID); }

    public List<String> getFavorites() {
        return favorites;
    }
    public List<String> getLikes() {
        return likes;
    }
}
