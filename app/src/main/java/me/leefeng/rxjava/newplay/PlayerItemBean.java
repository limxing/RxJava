package me.leefeng.rxjava.newplay;

import me.leefeng.rxjava.download.dbcontrol.FileHelper;

/**
 * Created by limxing on 2016/10/27.
 */

public class PlayerItemBean implements Comparable {
    private String path;
    private String name;
    private boolean isPlaying;
    private String url;
    private boolean isChecked;


    public PlayerItemBean(String s1, String formatUrl, String courseIndex, String s) {
        this.name = s1;
        this.url = formatUrl;
        this.path = FileHelper.getFileDefaultPath() + courseIndex + "/" + s + "/" + s1 + ".mp4";
    }

    public PlayerItemBean(String s1, String path) {
        this.name = s1;
        this.path = path;
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

    @Override
    public int compareTo(Object o) {
        return getIndex(this.getName()) - getIndex(((PlayerItemBean) o).getName());
    }

    /**
     * 获取
     *
     * @param name
     * @return
     */
    private int getIndex(String name) {
        String s = name.substring(0, name.indexOf(" "));
        String[] ss = s.split("\\.");
        if (ss.length == 2) {
            if (ss[1].length() == 1) {
                ss[1] = "0" + ss[1];
            }
            return Integer.parseInt(ss[0] + "" + ss[1]+"00");
        }
        if (ss[1].length() == 1) {
            ss[1] = "0"+ss[1]  ;
        }
        if (ss[2].length() == 1) {
            ss[2] =  "0"+ss[2] ;
        }

        return Integer.parseInt(ss[0] + "" + ss[1] + ss[2]);

    }
}
