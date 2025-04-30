import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    protected static final String MSG_SORTIR = "sortir";
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void iniciarServidor(){
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        try {
            serverSocket = new ServerSocket(PORT);
            clientSocket = serverSocket.accept();
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pararServidor()throws IOException{
        if (serverSocket != null) serverSocket.close();
        clientSocket.close();
        System.out.println("Servidor aturat.");
    }

    public String getNom(ObjectInputStream in)throws ClassNotFoundException, IOException{
        
        return (String) in.readObject();
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        ServidorXat servidorXat = new ServidorXat();
        servidorXat.iniciarServidor();
        ObjectOutputStream out = new ObjectOutputStream(servidorXat.clientSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(servidorXat.clientSocket.getInputStream());
        
        out.writeObject("Introdueix el teu nom: ");
        String nom = servidorXat.getNom(in);
        System.out.println("Nom del client: " + nom);
        FilServidorXat fxat = new FilServidorXat(in, nom);
        fxat.start();

        BufferedReader consola = new BufferedReader(new InputStreamReader(System.in));
        String missatge;

        do {
            missatge = consola.readLine();
            out.writeObject(missatge);
        } while (!missatge.equals(MSG_SORTIR));

        fxat.join();
        servidorXat.pararServidor();       
    }
}
