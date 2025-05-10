import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    protected static final String MSG_SORTIR = "sortir";
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public Socket connectar(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Acceptant connexions a ->" + HOST + ":" + PORT);
            System.out.println("Esperant connexió...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexió acceptada a ->" + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            System.out.println("Client connectat: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientSocket;
    }

    public void tancar(Socket socket)throws IOException{
        System.out.println("Tancant conexió amb el client" + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
        if (socket != null) socket.close();
    }

    public void enviarFitxers(ObjectInputStream in, ObjectOutputStream out)throws IOException, ClassNotFoundException{
        System.out.println("Esperant el nom del fitxer del client...");
        String nomFitxer = (String) in.readObject();
        System.out.println("Nom fitxer rebut: " + nomFitxer);
        if (nomFitxer.equalsIgnoreCase(MSG_SORTIR)) {
            System.out.println("Sortint del servidor...");
            tancar(clientSocket);
            return;
        }else {
            // Leer un fichero i enviar a client como byte[]
            //NomFitxer el nombre del fitxer en este servidor
            out.writeObject("Enviant fitxer: " + nomFitxer);
            Fitxer fitxer = new Fitxer(nomFitxer);
            byte[] fitxerBytes = fitxer.getContingut();
            System.out.println("MIda del fitxer en bytes: " + fitxerBytes.length);
            out.writeObject(fitxerBytes);
        }

    }

    public static void main(String[] args) {
        try {
            Servidor servidor = new Servidor();
            Socket socket = servidor.connectar();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            servidor.enviarFitxers(in, out);
            servidor.tancar(socket);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
