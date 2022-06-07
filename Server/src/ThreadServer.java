import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ThreadServer extends Thread {
    private Hashtable<String, ThreadClient> clientList;
    private Hashtable<String, String> clientNameList;
    private Hashtable<Integer, Map> clientGame;
    private Hashtable<String, Integer> clientPoint;
    private Hashtable<String, Integer> clientRoom;
    private Hashtable<Integer, Integer> SumPlayersRoom;
    private Hashtable<Integer, String> hostRoom;
    private Hashtable<Integer, Turn> turnOrder;
    private Integer IdRoom;
    private ServerSocket serverSocket;


    public ThreadServer() throws IOException {
        this.clientList = new Hashtable<>();
        this.clientNameList = new Hashtable<>();
        this.clientGame = new Hashtable<>();
        this.serverSocket = new ServerSocket(6666);
        this.clientPoint = new Hashtable<>();
        this.clientRoom = new Hashtable<>();
        this.SumPlayersRoom = new Hashtable<>();
        this.hostRoom = new Hashtable<>();
        this.turnOrder = new Hashtable<>();
        this.IdRoom = 0;
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
        this.SumPlayersRoom.put(this.IdRoom, game.getSumPlayer()-1);

        if(!this.clientGame.containsKey(this.IdRoom)){
            this.clientGame.put(this.IdRoom, new Map());
            System.out.println("Berhasil membuat room");

            this.clientRoom.put(clientName, this.IdRoom);
            System.out.println("Host Room "+ this.IdRoom + "= " + clientName);
            this.hostRoom.put(this.IdRoom, clientName);
        } else
            System.out.println("Gagal membuat room");

        Map map = this.clientGame.get(this.IdRoom);
        int[][] tanah = map.getTanah();
        int[][] ubi = map.getUbi();

        CariUbi app = new CariUbi(tanah, ubi);
        app.init();

        map.setTanah(app.getTanah());
        map.setUbi(app.getUbi());
        game.setTanah(app.getTanah());
        game.setUbi(app.getUbi());
        game.setPrintPetak(true);
        game.setIdRoom(this.IdRoom);
        game.setNewGame(true);

        this.clientPoint.put(game.getUsername(), 0);
        this.clientGame.replace(this.IdRoom, map);
        this.IdRoom = this.IdRoom + 1;
        System.out.println("masuk send");
        tc.sendGame(game);
    }

    public void startGame(Command cmd) throws IOException {
        Integer idRoom = cmd.getId();

        //set game to send it for the first time
        Map map = this.clientGame.get(cmd.getId());
        int[][] tanah = map.getTanah();
        int[][] ubi = map.getUbi();
        Game game = new Game();
        game.setTanah(map.getTanah());
        game.setUbi(map.getUbi());
        game.setEnterOperate(false);
        game.setNewGame(false);
        game.setPrintPetak(true);
        game.setIdRoom(idRoom);

        //assign room member to arraylist
        ArrayList<String> al = new ArrayList<>();
        Enumeration<String> e = this.clientRoom.keys();
        while (e.hasMoreElements()) {
            String username = e.nextElement();
            int roomUser = this.clientRoom.get(username);
            if(roomUser == idRoom) {
                al.add(username);
                String id = this.clientNameList.get(username);
                ThreadClient threadClient = this.clientList.get(id);
                threadClient.sendGame(game);
            }
        }
        //randomize turn
        Random r1 = new Random();
        for (int i = al.size() - 1; i >= 1; i--) {
            // swapping current index value
            // with random index value
            Collections.swap(al, i, r1.nextInt(i + 1));
        }

        Turn turn = new Turn();
        turn.setTurnOrder(al);
        turn.setIndex(0);

        this.turnOrder.put(idRoom, turn);
        System.out.println("masuk start");

        printTurn(idRoom);
    }

    public void printTurn(Integer idRoom) throws IOException {
        Turn turn = this.turnOrder.get(idRoom);
        ArrayList<String> al = turn.getTurnOrder();

        int index = turn.getIndex();

        String playerTurn = al.get(index);
        if(index + 1 < al.size()){
            turn.setIndex(index+1);
        }else{
            turn.setIndex(0);
        }
        this.turnOrder.replace(idRoom, turn);

        //send to room member with turn set
        YourTurn yourTurn = new YourTurn();
        Enumeration<String> e = this.clientRoom.keys();
        while (e.hasMoreElements()) {
            String username = e.nextElement();
            int roomUser = this.clientRoom.get(username);
            if(roomUser == idRoom) {
                yourTurn.setYourturn(username.equals(playerTurn));
                yourTurn.setPlayerTurn(playerTurn);
                yourTurn.setId(idRoom);
                String id = this.clientNameList.get(username);
                ThreadClient threadClient = this.clientList.get(id);
                threadClient.sendYourTurn(yourTurn);
            }
        }
    }
    public void playGame(Koordinat koordinat) throws IOException {
        Integer idRoom = koordinat.getIdRoom();
        Integer x = koordinat.getX();
        Integer y = koordinat.getY();

        String clientName = koordinat.getUsername();


        Map map = this.clientGame.get(koordinat.getIdRoom());
        int[][] tanah = map.getTanah();
        int[][] ubi = map.getUbi();

        tanah[y][x]=0;
        System.out.println(tanah[y][x]);
        System.out.println(ubi[y][x]);

        map.setTanah(tanah);
        map.setUbi(ubi);

        Game game = new Game();
        game.setTanah(tanah);
        game.setUbi(ubi);
        game.setIdRoom(idRoom);
        game.setNewGame(false);
        game.setPrintPetak(true);
        game.setEnterOperate(true);
        game.setUsername(clientName);
        game.setPoint(ubi[y][x]);


        if (ubi[y][x] == -1){
            game.setEnd(true);
            System.out.println("Permainan Selesai");
            Enumeration<String> e = this.clientRoom.keys();
            int maxPoint = 0;
            String minPlayer = "";
            int minPoint = 100;
            String maxPlayer = "";
            while (e.hasMoreElements()) {
                String username = e.nextElement();
                int roomUser = this.clientRoom.get(username);

                // send the message
                if(roomUser == idRoom) {
                    int pointPlayer = this.clientPoint.get(username);
                    if(pointPlayer > maxPoint){
                        maxPoint = pointPlayer;
                        maxPlayer = username;
                    }
                    if(pointPlayer < minPoint){
                        minPoint = pointPlayer;
                        minPlayer = username;
                    }
                }
            }
            String playerKalah = "";
            if(maxPoint > 40){
                playerKalah = maxPlayer;
            }else {
                playerKalah = minPlayer;
            }
            Message msg = new Message();
            msg.setText("Player kalah : " + playerKalah);
            game.setEndMessage("Player kalah : " + playerKalah);
            msg.setEnd(true);
            while (e.hasMoreElements()) {
                String username = e.nextElement();
                int roomUser = this.clientRoom.get(username);
                // send the message
                if(roomUser == idRoom) {
                    String id = this.clientNameList.get(username);
                    ThreadClient threadClient = this.clientList.get(id);
                    threadClient.send(msg);
                }
            }
        }

        this.clientGame.replace(koordinat.getIdRoom(), map);

        Enumeration<String> e = this.clientRoom.keys();
        while (e.hasMoreElements()) {
            String username = e.nextElement();
            int roomUser = this.clientRoom.get(username);
            if(roomUser == idRoom) {
                if(username.equals(clientName)){
                    game.setEnterOperate(true);
                }else{
                    game.setEnterOperate(false);
                }
                String clientId = this.clientNameList.get(username);
                ThreadClient tc = this.clientList.get(clientId);
                tc.sendGame(game);
            }
        }
    }

    public void changePoint(PlayerPoint playerpoint) throws IOException {
        Integer idRoom = playerpoint.getIdRoom();
        String username = playerpoint.getUsername();
        System.out.println((username));

        String operation = playerpoint.getOperation();
        Integer userPoint = this.clientPoint.get(username);

        System.out.println("before op " + userPoint);

        Integer value = playerpoint.getPoint();

        Player player = new Player();
        player.setPoint(userPoint);
        switch (operation){
            case "+" -> player.plus(value);
            case "-" -> player.minus(value);
            case "*" -> player.times(value);
            case "/" -> player.divide(value);
        }

        value = player.getPoint();
        System.out.println("after op " + value);
        playerpoint.setPoint(value);
        this.clientPoint.replace(username, value);

        String clientId = this.clientNameList.get(username);
        ThreadClient tc = this.clientList.get(clientId);
        tc.sendPoint(playerpoint);
        printTurn(idRoom);
    }

    public void joinRoom(Game game) throws IOException {
        Integer idRoom = game.getIdRoom();
        String username = game.getUsername();
        System.out.println("JOIN ROOM " + idRoom + " " + username);
        if (this.clientGame.containsKey(idRoom)){
            int sumPlayer = this.SumPlayersRoom.get(idRoom);
            this.SumPlayersRoom.replace(idRoom, sumPlayer-1);
            this.clientRoom.put(username, idRoom);
            this.clientPoint.put(game.getUsername(), 0);
            String clientId = this.clientNameList.get(username);
            ThreadClient tc = this.clientList.get(clientId);
            Message msg = new Message();

            //toRoom
            joinedNotify(msg, game);

            //to self
            if(sumPlayer-1 == 0){
                msg.setStart(true);
                msg.setId(idRoom);
                Enumeration<String> e = this.clientRoom.keys();
                while (e.hasMoreElements()) {
                    username = e.nextElement();
                    int roomUser = this.clientRoom.get(username);
//                    System.out.println(idRoom + " vs " + roomUser);
                    // send the message
                    if(roomUser == idRoom) {
                        String host = this.hostRoom.get(idRoom);
                        if(username.equals(host)){
                            msg.setHost(true);
                        }else
                            msg.setHost(false);
                        String id = this.clientNameList.get(username);
                        ThreadClient threadClient = this.clientList.get(id);
                        threadClient.send(msg);
                    }
                }
            }else{
                msg.setText(Log.ANSI_RED + "Waiting for host to start the game.." + Log.ANSI_RESET);
                msg.setSender("Room "+ idRoom);
                msg.setReceiver(username);
                msg.setRequest(false);
                msg.setChannel("3");
                msg.setStart(false);
                tc.send(msg);
            }
        }else{
            System.out.println("Room tidak ditemukan");
        }

    }

    public void joinedNotify(Message message, Game game) throws IOException {
        // iterate to all client
        Enumeration<String> e = this.clientRoom.keys();
        Integer idRoom = game.getIdRoom();
        String joinedPerson = game.getUsername();

//        System.out.println(message.getSender() + " : " + message.getText());
        System.out.println("Joined notify");
        while (e.hasMoreElements()) {
            String username = e.nextElement();
            int roomUser = this.clientRoom.get(username);
            System.out.println(idRoom + " vs " + roomUser);
                // send the message
            if(roomUser == idRoom) {
                message.setText(Log.ANSI_GREEN + joinedPerson + Log.ANSI_RESET + " has joined the room");
                message.setRequest(false);
                message.setChannel("4");
                message.setSender("[Server]");
                message.setStart(false);
                System.out.println("Mengirim message ke "+username);
                String clientId = this.clientNameList.get(username);
                ThreadClient threadClient = this.clientList.get(clientId);
                threadClient.send(message);
            }
        }
    }
}
