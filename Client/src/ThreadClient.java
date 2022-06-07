import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ThreadClient extends Thread {
    private ObjectInputStream objectInputStream;
    Read sc = new Read();
    private ObjectOutputStream objectOutputStream;
    Socket socket;


    public ThreadClient(ObjectInputStream objectInputStream, ObjectOutputStream objectOutputStream) {
        this.objectInputStream = objectInputStream;
        this.objectOutputStream = objectOutputStream;
    }

    @Override
    public void run() {
        while (true) {
            try{
                Object obj = this.objectInputStream.readObject();
                if(obj instanceof Message message){
                    if(!message.getStart()){
                        messageProcess(message);
                    }else{
                        System.out.println(Log.ANSI_GREEN + "Game bisa dimulai" + Log.ANSI_RESET);
                        if(message.getHost()){
                            System.out.println(Log.ANSI_BLUE + "You are the host. Type start play.." + Log.ANSI_RESET);
                            String start = sc.ReadString();
                            if(start.equalsIgnoreCase("start")){
                                Command cmd = new Command();
                                cmd.setStart(true);
                                cmd.setId(message.getId());
                                this.objectOutputStream.writeObject(cmd);
                                this.objectOutputStream.flush();
                                this.objectOutputStream.reset();
                            }
                        }
                    }
                }else if(obj instanceof Game game){
                    if(game.getNewGame()){
                        System.out.println(Log.ANSI_BLUE + "Room " + game.getIdRoom() + " telah dibuat" + Log.ANSI_RESET);
                        System.out.println(Log.ANSI_YELLOW + "Menunggu "+game.getSumPlayer()+ " player lain join" + Log.ANSI_RESET);
                    }else if (game.getPrintPetak() && !game.getEnterOperate()){
                        printPetak(game);
                    }else if(game.getPrintPetak() && game.getEnterOperate()){
                        printPetak(game);
                        System.out.println("Masukkan Operasi ( +, -, *, /)");
                        String operation = sc.ReadString();
                        Integer idRoom = game.getIdRoom();
                        String username = game.getUsername();
                        Integer point = game.getPoint();

                        System.out.println(Log.ANSI_BLUE + operation + point + Log.ANSI_RESET);
                        this.objectOutputStream.writeObject(PlayerPointObject.sendOperate(operation, username, point, idRoom));
                        this.objectOutputStream.flush();
                        this.objectOutputStream.reset();
                    }
                }else if(obj instanceof YourTurn yourTurn){
                    String playerTurn = yourTurn.getPlayerTurn();
                    Integer idRoom = yourTurn.getId();
                    if(yourTurn.isYourturn()){
                        System.out.println(Log.ANSI_BLUE + "Your turn" + Log.ANSI_RESET);
                        System.out.println("Masukkan Koordinat X Y");
                        Scanner scInt = new Scanner(System.in);
                        int x = scInt.nextInt();
                        int y = scInt.nextInt();

                        this.objectOutputStream.writeObject(KoordinatObject.sendKoordinat(x , y, playerTurn, idRoom));
                        this.objectOutputStream.flush();
                        this.objectOutputStream.reset();
                    }else{
                        System.out.println(Log.ANSI_RED + playerTurn + "'s turn" + Log.ANSI_RESET);
                    }
                }else{
                    System.out.println("object asing terdeteksi");
                    System.out.println(obj.getClass().getName());
                }
            }catch (IOException | ClassNotFoundException e) {
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
                else if(message.getChannel().equals("3")){
                    ch = "[Server]";
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
//            if(game.getNewGame()){
//                Read sc = new Read();
//                String username = sc.ReadString();
//            }
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

                this.objectOutputStream.writeObject(KoordinatObject.sendKoordinat(x,y,game.getUsername(),idRoom));
                this.objectOutputStream.flush();
                this.objectOutputStream.reset();

                printPoint = true;
            } else {
                System.out.println("Masukkan Operasi ( +, -, *, /)");
                String operation = sc.ReadString();
                String username = game.getUsername();
                Integer point = game.getPoint();

                System.out.println(Log.ANSI_BLUE + operation + point + Log.ANSI_RESET);
//                this.objectOutputStream.writeObject(PlayerPointObject.sendOperate(operation, username, point));
                this.objectOutputStream.flush();
                this.objectOutputStream.reset();
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
