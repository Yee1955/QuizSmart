package Class;

public class Interest {
    private String Id;
    private String Topic;

    public String getId() {
        return Id;
    }

    public String getTopic() {
        return Topic;
    }

    public void setId(String id) {
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
