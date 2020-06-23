package com.example.gesturedetector;

public class VideoInfo
{
    private int videono;
    private String topic;
    private String video;

    private String name;
    private String email;
    private String phone;

    public VideoInfo(int videono, String topic, String video, String name, String email, String phone) {
        this.videono = videono;
        this.topic = topic;
        this.video = video;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public int getVideono() {
        return videono;
    }

    public String getTopic() {
        return topic;
    }

    public String getVideo() {
        return video;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

}
