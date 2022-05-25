import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Read {
    private BufferedReader br;
    public String ReadString() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }
}


