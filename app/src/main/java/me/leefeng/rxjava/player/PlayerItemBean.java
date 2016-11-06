package me.leefeng.rxjava.player;

import com.limxing.library.utils.LogUtils;

/**
 * Created by limxing on 2016/10/27.
 */

public class PlayerItemBean {
    private String name;
    private boolean isPlaying;
    private String url;
    private boolean isChecked;

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
        String u = "C01S00P00";
        if (name.indexOf(" ") > 0) {
            String[] s = name.substring(0, name.indexOf(" ")).split("\\.");
            if (s.length > 0) {
                u = "C" + String.format("%02d", Integer.parseInt(s[0])) + "S" + String.format("%02d", Integer.parseInt(s[1]));
                if (s.length > 2) {
                    u = u + "P" + String.format("%02d", Integer.parseInt(s[2]));
                } else {
                    u = u + "P00";
                }
            }
        }
        return u;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
