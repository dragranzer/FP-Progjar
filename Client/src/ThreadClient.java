import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadClient extends Thread {
    private ObjectInputStream objectInputStream;
    Read sc = new Read();
    ObjectOutputStream objectOutputStream;
    Socket socket;


    public ThreadClient(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (true) {
            try {
                socket = new Socket("localhost", 6666);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                try {
                    System.out.println("masuk proc");
                    Object obj = this.objectInputStream.readObject();
                    if(obj instanceof Message message){
                        messageProcess(message);
                    }
                    if(obj instanceof Game game){
                        if(game.getNewGame()){
                            System.out.println("Ketik start untuk mulai:");
                        }
                        gameProcess(game);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void messageProcess(Message message){
        String ch = "";
            if (!message.getRequest()){
                if(message.getChannel().equals("1")){
                    ch = "[ALL]";
                }
                else if(message.getChannel().equals("2")){
                    ch = "[Chat]";
                }
                System.out.println(message.getSender() + ch +" : " + message.getText());
            }else{
                System.out.println("Online users : \n"+message.getText());
            }
    }

    public void gameProcess(Game game) throws IOException, ClassNotFoundException {
        int x = 0,y = 0;
        boolean printPoint = false;
        if(game.getPrintPetak()){
            if(game.getNewGame()){
                Read sc = new Read();
                String username = sc.ReadString();
            }
            printPetak(game);
        }
        do{
            if(!printPoint){

                System.out.println("Masukkan Koordinat X Y");
                Scanner scInt = new Scanner(System.in);
                x = scInt.nextInt();
                y = scInt.nextInt();

                if(x==-1 && y==-1)break;

                System.out.println("idRoom: " + game.getIdRoom());

                Integer idRoom = game.getIdRoom();

                objectOutputStream.writeObject(KoordinatObject.sendKoordinat(x,y,game.getUsername(),idRoom));
                objectOutputStream.flush();

                printPoint = true;
            } else {
                System.out.println("Masukkan Operasi ( +, -, *, /)");
                String operation = sc.ReadString();
                String username = game.getUsername();
                Integer point = game.getPoint();

                System.out.println(Log.ANSI_BLUE + operation + point + Log.ANSI_RESET);
                objectOutputStream.writeObject(PlayerPointObject.sendOperate(operation, username, point));
                objectOutputStream.flush();
                printPoint = false;
            }

            Object object = this.objectInputStream.readObject();
            if(object instanceof Game){
                game = (Game) object;
                printPetak(game);
                System.out.println(Log.ANSI_BLUE + "You get "+ game.getPoint() + Log.ANSI_RESET);
            }
            else if(object instanceof PlayerPoint playerPoint){
                printPetak(game);
                System.out.println(Log.ANSI_GREEN + "Your point is " + playerPoint.getPoint() + Log.ANSI_RESET);
            }
        }while(x!=-1 && y!=-1);
    }

    public void printPetak(Game game){
        int[][] tanah = game.getTanah();
        int[][] ubi = game.getUbi();

        System.out.print("\n   ");
        for(int c = 0; c<10; c++){
            System.out.format(Log.ANSI_YELLOW + "%2d " + Log.ANSI_RESET, c);
        }

        for(int a=0; a<10; a++){
            System.out.format(Log.ANSI_YELLOW + "\n%2d " + Log.ANSI_RESET, a);
            for(int b=0; b<10; b++){
                if(ubi[a][b] == -1)
                    System.out.format("%2c ", '-');
                else if(tanah[a][b]=='-')
                    System.out.format("%2c ", tanah[a][b]);
                else if(tanah[a][b]==0)
                    System.out.format(" %d ", ubi[a][b]);
            }
        }
        System.out.println("\n");
    }
}
