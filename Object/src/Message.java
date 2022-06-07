import java.io.Serializable;

public class Message implements Serializable {
    private String text;
    private String sender;
    private String receiver;
    private String channel;
    private Boolean request;
    private Boolean isStart;
    private Boolean host;
    private Integer id;
    private Boolean isEnd;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Boolean getRequest() {
        return request;
    }

    public void setRequest(Boolean request) {
        this.request = request;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public Boolean getHost() {
        return host;
    }

    public void setHost(Boolean host) {
        this.host = host;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEnd() {
        return isEnd;
    }

    public void setEnd(Boolean end) {
        isEnd = end;
    }
}