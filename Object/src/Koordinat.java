import java.io.Serializable;

public class Koordinat implements Serializable {
    private Integer x;
    private Integer y;
    private String username;
    private String operation;
    private int[][] tanah;
    private int[][] ubi;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int[][] getTanah() {
        return tanah;
    }

    public void setTanah(int[][] tanah) {
        this.tanah = tanah;
    }

    public int[][] getUbi() {
        return ubi;
    }

    public void setUbi(int[][] ubi) {
        this.ubi = ubi;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
