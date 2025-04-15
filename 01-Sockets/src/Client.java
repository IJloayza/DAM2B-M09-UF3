import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static final int PORT = 7777;
    private static final String HOST = "localhost";
    private Socket socket;
    private PrintWriter out;

    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Connectat a servidor en " + HOST + ":" + PORT);
    }

    public void envia(String missatge) {
        out.println(missatge);
        System.out.println("Enviat al servidor: " + missatge);
    }

    public void tanca() throws IOException {
        if (out != null) out.close();
        if (socket != null) socket.close();
        System.out.println("Client tancat");
    }

    public static void main(String[] args) {
        BufferedReader Str = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client();
        try {
            client.connecta();
            client.envia("Prova d'enviament 1");
            client.envia("Prova d'enviament 2");
            client.envia("Adeu!");

            System.out.println("Prem Enter per tancar el client...");
            Str.readLine();

            client.tanca();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
