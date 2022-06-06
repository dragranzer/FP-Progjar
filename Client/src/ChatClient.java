import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
                ThreadClient threadClient = new ThreadClient(new ObjectInputStream(socket.getInputStream()));
                threadClient.start();

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
                            objectOutputStream.writeObject(GameObject.newGame(username));
                            objectOutputStream.flush();

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
