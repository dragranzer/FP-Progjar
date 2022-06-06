import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ThreadServer threadServer;

    public ThreadClient(Socket socket, ThreadServer threadServer) {
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.threadServer = threadServer;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Message message) throws IOException {
        this.objectOutputStream.writeObject(message);
        this.objectOutputStream.flush();
    }

    public void sendGame(Game game) throws IOException {
        this.objectOutputStream.writeObject(game);
        this.objectOutputStream.flush();
        this.objectOutputStream.reset();
    }

    public void sendPoint(PlayerPoint playerPoint) throws IOException {
        this.objectOutputStream.writeObject(playerPoint);
        this.objectOutputStream.flush();
        this.objectOutputStream.reset();
    }

    @Override
    public void run() {
        while (true) {
            // jalan tiap kali chat client di enter
            try {
                Object obj = this.objectInputStream.readObject();
                if(obj instanceof Message message){
                    if(message.getRequest()) {
                        this.threadServer.getOnlineUser(message);
                    }
                    else{
                        this.threadServer.sendMessage(message, message.getReceiver(), message.getSender());
                    }
                }
                else if(obj instanceof User user){
                    if(user.getStatus()){
                        this.threadServer.setNameID(user);
                    }
                    else {
                        this.threadServer.logout(user);
                    }
                }else if(obj instanceof Game game){
                    if(game.getNewGame())
                        this.threadServer.createGame(game);
                    else
                        this.threadServer.joinRoom(game);
                }else if(obj instanceof Koordinat koordinat){
                    System.out.println("x : "+koordinat.getX());
                    System.out.println("id room : "+koordinat.getIdRoom());
                    this.threadServer.playGame(koordinat);
                }else if(obj instanceof PlayerPoint playerPoint){
                    System.out.println("masuk");
                    System.out.println(playerPoint.getUsername());
                    this.threadServer.changePoint(playerPoint);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}
