package me.leefeng.rxjava.newplay;

import me.leefeng.rxjava.download.dbcontrol.FileHelper;

/**
 * Created by limxing on 2016/10/27.
 */

public class PlayerItemBean {
    private  String path;
    private String name;
    private boolean isPlaying;
    private String url;
    private boolean isChecked;

    public PlayerItemBean(String s, String formatUrl) {
        this.name = s;
        this.url = formatUrl;
    }

    public PlayerItemBean(String s1, String formatUrl, String courseIndex, String s) {
        this.name = s1;
        this.url = formatUrl;
        this.path = FileHelper.getFileDefaultPath()+courseIndex + "/" + s+"/"+s1+".mp4";
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
        return url;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
