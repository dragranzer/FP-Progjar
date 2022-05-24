import java.io.IOException;

public class ChatServer {
    public static void main(String[] args) {
        try {
            ThreadServer threadServer = new ThreadServer();
            threadServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
