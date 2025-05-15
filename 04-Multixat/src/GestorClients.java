import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ServidorXat servidorXat;
    private boolean sortir = false;

    public GestorClients(String nom, Socket socket, ServidorXat servidorXat) {
        super(nom);
        this.socket = socket;
        this.servidorXat = servidorXat;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNomClient() {
        return this.getName();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
    }

    public void enviarMissatge(String remitent, Missatge missatge){

    }

    public void processarMissatge(Missatge missatge){
        switch (missatge.getTipus()){
            case Missatge.CODI_CONECTAR:
                
                break;
            case Missatge.CODI_SORTIR_CLIENT:
                
                break;
            case Missatge.CODI_SORTIR_TOTS:
                
                break;
            case Missatge.CODI_MSG_PERSONAL:
                
                break;
            default:
                System.out.println("Tipus de missatge desconegut");
        }
    }
}
