import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Scanner {
    private BufferedReader br;
    public String Scanner() throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        return br.readLine();
    }

}
