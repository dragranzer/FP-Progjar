import java.io.Serializable;

public class Command implements Serializable {
    private Boolean start;
    private Integer id;

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
