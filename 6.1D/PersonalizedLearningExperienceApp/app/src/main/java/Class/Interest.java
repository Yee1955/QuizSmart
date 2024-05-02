package Class;

import java.io.Serializable;

public class Interest implements Serializable {
    private long Id;
    private String Topic;

    public long getId() {
        return Id;
    }

    public String getTopic() {
        return Topic;
    }

    public void setId(long id) {
        Id = id;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }
    public Interest(){

    }

    public Interest(String topic) {
        Topic = topic;
    }
}
