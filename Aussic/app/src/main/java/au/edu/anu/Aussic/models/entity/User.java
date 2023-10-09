package au.edu.anu.Aussic.models.entity;


import java.util.List;

//用户注册后新建user上传firestore
//登陆之后把用户id传给homeactivity，实例化个人主页fragment的时候再传，然后在fragment初始化的时候根据用户id从firebastore获取用户信息展示出来
public class User {
    String username;
    List<String> farorites; //放歌曲id
    String gps;
    String iconUrl;

    public void update(){};  //更新数据库里的user数据

}
