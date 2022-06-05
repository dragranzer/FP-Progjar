import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
    private ObjectInputStream objectInputStream;

    public ThreadClient(ObjectInputStream objectInputStream, String username) {
        this.objectInputStream = objectInputStream;
    }

    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = new Socket("localhost", 6666);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Read sc = new Read();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());


                String ch = "";
                try {
                    Object obj = this.objectInputStream.readObject();
                    if(obj instanceof Message message){
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
                    if(obj instanceof Game game){
                        if(game.getPrintPetak()){
                            System.out.println("masuk");
                            int[][] tanah = game.getTanah();
                            int[][] ubi = game.getUbi();
                            Boolean isOperate = game.getEnterOperate();
                            if(isOperate == null){
                                isOperate = false;
                            }
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
                            if(!isOperate)System.out.println("Masukkan Koordinat X Y");
                            else {
                                System.out.println("Masukkan Operasi");
                                String operation = sc.ReadString();
                                System.out.println(operation);
                                String username = game.getUsername();
                                Integer point = game.getPoint();
                                objectOutputStream.writeObject(PlayerPointObject.sendOperate(operation, username, point));
                            }
                        }
                    }else if(obj instanceof Koordinat koordinat){

                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
