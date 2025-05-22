import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    protected static final int PORT = 9999;
    protected static final String HOST = "localhost";
    private static String MSG_SORTIR = "sortir";
    private static Hashtable <String, GestorClients> clients = new Hashtable<>();
    private boolean sortir = false;
    private static ServerSocket servidor;

    public ServidorXat() {}

    public void servidorAEscoltar() {
        try {
            servidor = new ServerSocket(PORT);
            System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pararServidor(){
        if (servidor != null){
            try {
                servidor.close();
                System.out.println("Servidor aturat.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    public void finalitzarXat() {
        enviarMissatgeGrup(MSG_SORTIR);
        clients.clear();
        sortir = true;
        pararServidor();
        System.out.println("Xat finalitzat.");
    }

    public void afegirClient(GestorClients gestor) {
        clients.put(gestor.getName(), gestor);
        System.out.println(gestor.getName() + " connectat.");
        System.out.println("DEBUG: multicast Entra: " + gestor.getName());
    }

    public void eliminarClient(String name) {
        if (clients.containsKey(name)) {
            clients.remove(name);
            System.out.println("Client eliminat: " + name);
        }
    }

    public void enviarMissatgeGrup(String message) {
        for (GestorClients gestor : clients.values()) {
            gestor.enviarMissatge("Grup", message);
            System.out.println("Missatge grupal: " + message);
        }
    }

    public void enviarMissatgePersonal(String recipient, String sender, String message) {
        GestorClients gestor = clients.get(recipient);
        if (gestor != null) {
            gestor.enviarMissatge(sender, message);
            System.out.println("Missatge personal per (" + recipient + ") de (" + sender + "): " + message);
        } else System.out.println("Destinatari no trobat: " + recipient);
    }

    public static void main(String[] args) {
        ServidorXat server = new ServidorXat();
        server.servidorAEscoltar();

        while (!server.sortir) {
            try {
                Socket client = servidor.accept();
                System.out.println("Client connectat: " + ServidorXat.HOST + ":" + ServidorXat.PORT);
                GestorClients gestor = new GestorClients("initGestor", client, server);
                gestor.start();
            } catch (IOException e) {
                if (!server.sortir) e.printStackTrace();
            }
        }

        server.pararServidor();
    }
}
