package com.example.dispositivosmoveis;

public class UserHelperClass {
    String nickname, score;

    public UserHelperClass() {

    }

    public UserHelperClass(String nickname, String score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String name)
    {
        nickname = name;
    }

    public String getScore()
    {
        return score;
    }

    public void setScore(String points)
    {
        score = points;
    }
}
