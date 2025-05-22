import java.io.EOFException;
import java.io.IOException;
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
            sortir = true;//Si fallan los streams debe salir...?
        }
    }

    public String getNomClient() {
        return this.getName();
    }

    @Override
    public void run() {
        try {
            while (!sortir) {
                String message = (String) in.readObject();
                processaMissatge(message);
            }
        }catch(EOFException e){
            System.out.println("Client " + getName() + " tancat.");
        } catch (IOException e) {
            System.err.println("Error d'entrada/sortida: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            try {
                servidorXat.eliminarClient(getName());
                if (socket!=null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void enviarMissatge(String recipient, String message) {
        try {
            if (recipient.equals("Grup")) {
                out.writeObject(Missatge.getMissatgeGrup(message));
                out.flush();
            } else {
                out.writeObject(Missatge.getMissatgePersonal(recipient, message));
                out.flush();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processaMissatge(String rawMessage) {
        String code = Missatge.getCodiMissatge(rawMessage);
        String[] codeParts = Missatge.getPartsMissatge(rawMessage);
        switch (code) {
            case Missatge.CODI_CONECTAR : {
                super.setName(codeParts[1]);
                servidorXat.afegirClient(this);
                break;
            }
            case Missatge.CODI_MSG_PERSONAL : {
                String recipient = codeParts[1];
                String message = codeParts[2];
                servidorXat.enviarMissatgePersonal(recipient, getName(), message);
                break;
            }
            case Missatge.CODI_MSG_GRUP : {
                String message = codeParts[1];
                servidorXat.enviarMissatgeGrup(message);
                break;
            }
            case Missatge.CODI_SORTIR_CLIENT : {
                this.sortir = true;
                servidorXat.eliminarClient(getName());
                break;
            }
            case Missatge.CODI_SORTIR_TOTS : {
                this.sortir = true;
                servidorXat.finalitzarXat();
                break;
            }
            default : {
                System.err.println("Codi no disponible: " + code);
            }
        }
    }
}
