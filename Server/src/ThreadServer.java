import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

public class ThreadServer extends Thread {
    private Hashtable<String, ThreadClient> clientList;
    private Hashtable<String, String> clientNameList;
    private Hashtable<String, Map> clientGame;
    private ServerSocket serverSocket;
    private Hashtable<String, Integer> clientPoint;

    public ThreadServer() throws IOException {
        this.clientList = new Hashtable<>();
        this.clientNameList = new Hashtable<>();
        this.clientGame = new Hashtable<>();
        this.serverSocket = new ServerSocket(6666);
        this.clientPoint = new Hashtable<>();
    }

    @Override
    public void run() {
        // listen for a new connection
        while (true) {
            // accept a new connection
            try {
                Socket socket = this.serverSocket.accept();

                // create a new ThreadClient
                ThreadClient threadClient = new ThreadClient(socket, this);
                // start the new thread
                threadClient.start();

                // store the new thread to the hashtable
                String clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                this.clientList.put(clientId, threadClient);
                this.clientPoint.put(clientId, 0);
//                System.out.println("Finish");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendToAll(Message message) throws IOException {
        // iterate to all client
        Enumeration<String> e = this.clientList.keys();
        System.out.println(message.getSender() + " : " + message.getText());

        while (e.hasMoreElements()) {
            String clientId = e.nextElement();
            // send the message
            ThreadClient threadClient = this.clientList.get(clientId);
            threadClient.send(message);
        }
    }

    public void sendMessage(Message message, String dst, String src) throws IOException {
        if(clientNameList.containsKey(dst)) {
            String clientId = clientNameList.get(dst);
            String me = clientNameList.get(src);
            ThreadClient tc = this.clientList.get(me);
            tc.send(message);
            tc = this.clientList.get(clientId);
            tc.send(message);
        }else if(dst.equalsIgnoreCase("All")){
            sendToAll(message);
        }
        else{
            System.out.println("User Tidak Ada");
        }
    }

    public void setNameID(User user) throws IOException {
        Enumeration<String> e = this.clientList.keys();
        String nameClient = user.getName();

        while (e.hasMoreElements()) {
            String clientId = e.nextElement();
            if (!this.clientNameList.containsValue(clientId)){
                this.clientNameList.put(nameClient, clientId);
                this.clientGame.put(nameClient, new Map());
            }
        }
        System.out.println(this.clientNameList);
    }

    public void getOnlineUser(Message message) throws IOException {
        String clientName = message.getSender();
        String clientId = this.clientNameList.get(clientName);

        Hashtable<String, String> hashtable = clientNameList;
        Set<String> keys = hashtable.keySet();
        String usersOnline = "";

        for(String key: keys){
            usersOnline = usersOnline.concat(key);
            usersOnline = usersOnline.concat("\n");
        }
        message.setText(usersOnline);

        ThreadClient tc = this.clientList.get(clientId);
        tc.send(message);
    }

    public void logout(User user) throws IOException {
        String nameClient = user.getName();
        String clientId = this.clientNameList.get(nameClient);
        ThreadClient tc = this.clientList.get(clientId);

        this.clientNameList.remove(nameClient);
        this.clientList.remove(clientId);

        Message msg = new Message();
        msg.setText("Logout..");
        tc.send(msg);

        System.out.println(nameClient +": " + clientId + " has logged out");
        System.out.println(this.clientNameList);
    }

    public void createGame(Game game) throws IOException {
        String clientName = game.getUsername();
        String clientId = this.clientNameList.get(clientName);
        ThreadClient tc = this.clientList.get(clientId);

        Map map = this.clientGame.get(clientName);
        int[][] tanah = map.getTanah();
        int[][] ubi = map.getUbi();

        CariUbi app = new CariUbi(tanah, ubi);
        app.init();

        map.setTanah(app.getTanah());
        map.setUbi(app.getUbi());
        game.setTanah(app.getTanah());
        game.setUbi(app.getUbi());
        game.setPrintPetak(true);

        this.clientGame.replace(clientName, map);
        tc.sendGame(game);
    }

    public void playGame(Koordinat koordinat) throws IOException {
        Integer x = koordinat.getX();
        Integer y = koordinat.getY();

        String clientName = koordinat.getUsername();
        String clientId = this.clientNameList.get(clientName);
        ThreadClient tc = this.clientList.get(clientId);

        Map map = this.clientGame.get(clientName);
        int[][] tanah = map.getTanah();
        int[][] ubi = map.getUbi();

        tanah[y][x]=0;
        System.out.println(tanah[y][x]);

        map.setTanah(tanah);
        map.setUbi(ubi);

        Game game = new Game();
        game.setTanah(tanah);
        game.setUbi(ubi);
        game.setPrintPetak(true);
        game.setEnterOperate(true);
        game.setUsername(clientName);
        game.setPoint(ubi[y][x]);

        this.clientGame.replace(clientName, map);
        tc.sendGame(game);
    }

    public void changePoint(PlayerPoint playerpoint) throws IOException {
        String username = playerpoint.getUsername();
        System.out.println((username));
    }
}
