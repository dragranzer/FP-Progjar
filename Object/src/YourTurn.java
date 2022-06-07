import java.io.Serializable;

public class YourTurn implements Serializable{
    private boolean isYourturn;
    private String playerTurn;

    private Integer id;

    public boolean isYourturn() {
        return isYourturn;
    }

    public void setYourturn(boolean yourturn) {
        isYourturn = yourturn;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
