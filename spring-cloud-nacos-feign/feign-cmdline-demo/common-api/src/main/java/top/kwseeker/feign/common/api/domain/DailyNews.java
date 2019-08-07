package top.kwseeker.feign.common.api.domain;

public class DailyNews {

    private String topic;
    private String picUrl;
    private String contentUrl;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return "DailyNews{" +
                "topic='" + topic + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                '}';
    }
}
