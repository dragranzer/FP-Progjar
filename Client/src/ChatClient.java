import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        boolean online;
        Read sc = new Read();
        try {
//            while (true) {
                Socket socket = new Socket("localhost", 6666);

                Cli.login();
                String username = sc.ReadString();
                online = true;

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//                ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()));
//                threadClient.start();

                objectOutputStream.writeObject(UserObject.getLogin(username));
                objectOutputStream.flush();

//                do {
                    Cli.welcome();
                    String task = sc.ReadString();
                    switch (task) {
                        case "1" -> {
                            Cli.channel();
                            String choice = sc.ReadString();

                            Cli.typeMsg();
                            String message = sc.ReadString();

                            String receiver;
                            if (choice.equals("2")) {
                                System.out.println("Send To: ");
                                receiver = sc.ReadString();
                            } else {
                                receiver = "all";
                            }

                            objectOutputStream.writeObject(MessageObject.getMsg(username, receiver, choice, message));
                            objectOutputStream.flush();
                        }
                        case "2" -> {
//                            System.out.println("OK");
                            objectOutputStream.writeObject(MessageObject.getList(username));
                            objectOutputStream.flush();
                        }
                        case "3" -> {
                            System.out.println("Apakah anda ingin:");
                            System.out.println("1. Create Room");
                            System.out.println("2. Join Room");
                            Integer choice;
                            Scanner scInt = new Scanner(System.in);
                            choice = scInt.nextInt();
                            if(choice == 1){
                                System.out.println("Masukkan jumlah player:");
                                Integer sumPlayer = scInt.nextInt();
                                objectOutputStream.writeObject(GameObject.newGame(username, sumPlayer));
                                objectOutputStream.flush();
                                objectOutputStream.reset();
                                ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()), objectOutputStream);
                                threadClient.start();

                            }else if(choice == 2){
                                System.out.println("Masukkan id Room:");
                                Integer idRoom = scInt.nextInt();
                                objectOutputStream.writeObject(GameObject.joinGame(idRoom,username));
                                objectOutputStream.flush();
                                objectOutputStream.reset();
                                ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()), objectOutputStream);
                                threadClient.start();

                            }
                        }
                        case "4" -> {
                            objectOutputStream.writeObject(UserObject.getLogout(username));
                            objectOutputStream.flush();

                            online = false;
                        }
                    }
//                } while (online);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
