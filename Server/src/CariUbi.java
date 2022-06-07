import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class CariUbi {
    private int[][] tanah;
    private int[][] ubi;

    public CariUbi(int[][] tanah, int[][] ubi){
        this.tanah = tanah;
        this.ubi = ubi;
    }

    public void set1Ubi(int x, int y, int value) {
        ubi[x][y] = value;
    }

    public int get1Ubi(int x, int y) {
        return ubi[x][y];
    }

    public int get1Tanah(int x, int y) {
        return tanah[x][y];
    }

    public void set1Tanah(int x, int y, int value) {
        tanah[x][y] = value;
    }

    public int[][] getTanah() {
        return tanah;
    }

    public int[][] getUbi() {
        return ubi;
    }

    public void cariubi() {
        int ubii=10, x, y;
        Player player = new Player();
        player.setPoint(0);

        while(ubii!=0){
            printPetak();

            System.out.println("Poin kamu saat ini : " + player.getPoint());
            System.out.print("Masukkan Koordinat Gali Dengan Format 'x y' : ");
            Scanner sc = new Scanner(System.in);
            x = sc.nextInt();
            y = sc.nextInt();

            tanah[x][y] = ubi[x][y]+48;
            System.out.println(tanah[x][y] + " " + ubi[x][y]);

            if(ubi[x][y]== -1){
//                ubii--;
                ubii = 0;
                printPetak();
                continue;
            }

            System.out.println(Log.ANSI_GREEN + "Selamat!! Kamu mendapatkan "+ ubi[x][y] + " poin dari hasil menggali" + Log.ANSI_RESET);
            operation(player, ubi[x][y]);
//            System.out.println("Ubi " +tanah[x][y]);
        }
        System.out.println("Poin akhir kamu : "+ player.getPoint());
    }

    public void init() {
        for (int[] row: tanah)
            Arrays.fill(row, 0);
        for (int[] row: ubi)
            Arrays.fill(row, 0);

        Random rand = new Random();

        for (int a=0;a<10;a++)
        {
            for (int b=0;b<10;b++)
            {
                ubi[a][b]=Math.abs(rand.nextInt())%10;
            }
        }
        //gambar skema tanah yang masih penuh atau masih belum digali dengan tanda 'X'
        for (int a=0;a<10;a++)
        {
            for (int b=0;b<10;b++)
            {
                tanah[a][b]='-';
            }
        }

            int n=Math.abs(rand.nextInt())%10;
            int y=Math.abs(rand.nextInt())%10;
            //setelah ditemukan koordinat randomnya, jadikan array ubi pada koordinat n,y menjadi 'O' yang menandakan adanya ubi
            ubi[n][y]= -1;
            System.out.println("Ending point "+y+" "+n);
            //masuk fungsi bermain
//            cariubi();
    }

    public void operation(Player player, Integer value){
        int choice;
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                    Pilih Operasi yang Diingankan
                    1. Tambah
                    2. Kurang
                    3. Kali
                    4. Bagi
                    """);
        choice = sc.nextInt();
        switch (choice){
            case 1 -> player.plus(value);
            case 2 -> player.minus(value);
            case 3 -> player.times(value);
            case 4 -> player.divide(value);
        }
        System.out.println("Point kamu menjadi : " + player.getPoint());
    }

    public void printPetak(){
        System.out.print("\n   ");
        for(int c = 0; c<10; c++){
            System.out.format(Log.ANSI_YELLOW + "%2d " + Log.ANSI_RESET, c);
        }

        for(int a=0; a<10; a++){
            System.out.format(Log.ANSI_YELLOW + "\n%2d " + Log.ANSI_RESET, a);
            for(int b=0; b<10; b++){
                if(ubi[a][b] == -1)
                    System.out.format("%2c ", '-');
                else
//                    System.out.format("%2d ", ubi[a][b]);
                    System.out.format("%2c ", tanah[a][b]);
//                    System.out.print(tanah[a][b]+" ");
            }
        }
        System.out.println("\n");
    }
}
