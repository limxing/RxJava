package me.leefeng.rxjava.main;

import java.util.List;

/**
 * Created by limxing on 2016/10/28.
 */

public class Course {
    private String name;
    private String pic;
    private String teacher;
    private String id;
    private List<String> catelogue;
    private List<List<String>> videos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCatelogue() {
        return catelogue;
    }

    public void setCatelogue(List<String> catelogue) {
        this.catelogue = catelogue;
    }

    public List<List<String>> getVideos() {
        return videos;
    }

    public void setVideos(List<List<String>> videos) {
        this.videos = videos;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", teacher='" + teacher + '\'' +
                ", id='" + id + '\'' +
                ", catelogue=" + catelogue +
                ", videos=" + videos +
                '}';
    }
}
