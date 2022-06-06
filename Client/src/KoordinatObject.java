public class KoordinatObject {
    public static Koordinat sendKoordinat(Integer x, Integer y, String username) {
        Koordinat koordinat = new Koordinat();
        koordinat.setX(x);
        koordinat.setY(y);
        koordinat.setUsername(username);
        return koordinat;
    }
}
