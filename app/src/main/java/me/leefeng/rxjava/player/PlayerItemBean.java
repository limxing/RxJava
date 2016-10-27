package me.leefeng.rxjava.player;

/**
 * Created by limxing on 2016/10/27.
 */

public class PlayerItemBean {
    private String name;
    private boolean isPlaying;
    private String url;

    public PlayerItemBean() {
    }

    public PlayerItemBean(String s) {
        this.name = s;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getUrl() {
        String u = "C00S00P00";
        String[] s = name.substring(0, name.indexOf(" ")).split(".");
        if (s.length > 0) {
            u = "C" + String.format("%04d", s[0]) + "S" + String.format("%04d", s[1]);
            if (s.length > 2) {
                u = u + "P" + String.format("%04d", s[2]);
            }
        }
        return u;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
