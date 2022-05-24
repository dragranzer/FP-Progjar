public class Map {
    int[][] tanah = new int[100][100];
    int[][] ubi = new int[100][100];

    public void setTanah(int[][] tanah) {
        this.tanah = tanah;
    }

    public int[][] getTanah() {
        return tanah;
    }

    public int[][] getUbi() {
        return ubi;
    }

    public void setUbi(int[][] ubi) {
        this.ubi = ubi;
    }
}
