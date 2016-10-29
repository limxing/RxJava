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
    private List<Video> videos;


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

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public class Video {
        private String url;
        private List<String> values;

        public List<String> getValues() {
            return values;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Video{" +
                    "url='" + url + '\'' +
                    ", values=" + values +
                    '}';
        }
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
