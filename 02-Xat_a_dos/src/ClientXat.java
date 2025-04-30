import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientXat {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private BufferedReader std = new BufferedReader(new InputStreamReader(System.in));

    public void connecta() throws IOException {
        socket = new Socket(HOST, PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        
        System.out.println("Client connectat a " + HOST + ":" + PORT);
    }

    public void enviarMissatge(String missatge)throws IOException{
        out.writeObject(missatge);
        System.out.println("Enviant missatge: " + missatge);
    }

    public void tancarClient() throws IOException {
        System.out.println("Tancant client...");
        if (in != null) in.close();
        if (out != null) out.close();
        if (std != null) std.close();
        if (socket != null) socket.close();
        System.out.println("Client tancat.");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ClientXat client = new ClientXat();
        client.connecta();
        System.out.println("Flux d'entrada i sortida creat.");

        FilLectorCX fil = new FilLectorCX(client.in);
        fil.start();

        String missatge;
            do {
                missatge = client.std.readLine();
                client.enviarMissatge(missatge);
            } while (!missatge.equals(ServidorXat.MSG_SORTIR));

        fil.join();
        client.tancarClient();
    }
}
