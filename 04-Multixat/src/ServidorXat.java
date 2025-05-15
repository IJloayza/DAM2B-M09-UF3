import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;

public class ServidorXat {
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static String MSG_SORTIR = "sortir";
    private static Hashtable <String, GestorClients> clients = new Hashtable<>();
    private static boolean sortir = false;
    private static ServerSocket servidor;
    public void servidorAEscoltar(){

    }

    public void pararServidor(){
        if (servidor != null){
            try {
                servidor.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    public void finalitzarXat(){

    }

    public void afegirClient(GestorClients client){

    }

    public void eliminarClient(String nomClient){

    }

    public void enviarMissatgeGrup(Missatge missatge){

    }

    public void enviarMissatgePersonal(){
        
    }
}
