import java.io.Serializable;

public class Game implements Serializable {
    private int[][] tanah;
    private int[][] ubi;

    private Boolean printPetak;
    private Boolean newGame;
    private String username;

    private int x;
    private int y;



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

    public Boolean getPrintPetak() {
        return printPetak;
    }

    public void setPrintPetak(Boolean printPetak) {
        this.printPetak = printPetak;
    }

    public Boolean getNewGame() {
        return newGame;
    }

    public void setNewGame(Boolean newGame) {
        this.newGame = newGame;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}