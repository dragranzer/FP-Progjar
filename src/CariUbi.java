import java.util.*;

public class CariUbi {
    static int[][] tanah = new int[100][100];
    static int[][] ubi = new int[100][100];;
    static int[][] udah = new int[100][100];;

    public static void cariubi() {
        int ubii=10, x, y;

        while(ubii!=0){
            System.out.print("   ");
            for(int c=0; c<25; c++){
                System.out.format("%2d ", c);
            }
            System.out.println();

            for(int a=0; a<25; a++){
                System.out.format("\n%2d ", a);
                for(int b=0; b<25; b++){
                    System.out.format("%2c ", tanah[a][b]);
//                    System.out.print(tanah[a][b]+" ");
                }
            }
            System.out.println();
            System.out.print("Masukkan Koordinat Gali Dengan Format (Koordinatx<SPASI>Koordinaty) : ");
            Scanner scan= new Scanner(System.in);
            x = scan.nextInt();
            y = scan.nextInt();

            tanah[x][y]=ubi[x][y]+48;
//            System.out.println("Ubi " +tanah[x][y]);
            if(tanah[x][y]=='O')ubii--;
        }
        return;
    }

    public static void main(String[] args) {
        for (int[] row: tanah)
            Arrays.fill(row, 0);
        for (int[] row: ubi)
            Arrays.fill(row, 0);
        for (int[] row: udah)
            Arrays.fill(row, 0);

        int jumlah=0;
        Random rand = new Random();

        for (int a=0;a<25;a++)
        {
            for (int b=0;b<25;b++)
            {
                ubi[a][b]=Math.abs(rand.nextInt())%10;
            }
        }
        //gambar skema tanah yang masih penuh atau masih belum digali dengan tanda 'X'
        for (int a=0;a<25;a++)
        {
            for (int b=0;b<25;b++)
            {
                tanah[a][b]='X';
            }
        }

        for (int a=0;a<10;a++)
        {
            int n=Math.abs(rand.nextInt())%25;
            int y=Math.abs(rand.nextInt())%25;
            //untuk mengecek jika pada lokasi sudah ditemukan ubi
            if(udah[n][y]!=0)
            {
                //jika ya, ulangi looping dengan a yang sama seperti sebelumnya
                a--;
                continue;
            }
            //setelah ditemukan koordinat randomnya, jadikan array ubi pada koordinat n,y menjadi 'O' yang menandakan adanya ubi
            ubi[n][y]='O';
            //menandai bahwa sudah terdapat ubi pada koordinat n,y
            udah[n][y]++;
        }
        //masuk fungsi bermain
        cariubi();

        return;
    }
}
