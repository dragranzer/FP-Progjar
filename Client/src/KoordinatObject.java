public class KoordinatObject {
    public static Koordinat sendKoordinat(Integer x, Integer y) {
        System.out.println(x+y);
        Koordinat koordinat = new Koordinat();
        koordinat.setX(x);
        koordinat.setY(y);
        return koordinat;
    }
}
