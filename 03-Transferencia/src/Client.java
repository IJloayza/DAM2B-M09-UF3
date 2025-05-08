import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private String DIR_ARRIBADA = "D:\\Temp";
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static final int PORT = 9999;
    private static final String HOST = "localhost";
    private static BufferedReader std = new BufferedReader(new InputStreamReader(System.in));

    public void connectar() throws IOException{
        System.out.println("Connectant a -> " + HOST + ":" + PORT);
        socket = new Socket(HOST, PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public void rebreFitxers(String nomFitxer, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.writeObject(nomFitxer);
        String nomFitxerRebut = (String) in.readObject();
        System.out.println("Nom del fitxer a guardar: " + nomFitxerRebut);    
        System.out.println("Rebent fitxer: " + nomFitxerRebut);
        
        Fitxer fitxer = new Fitxer(nomFitxerRebut);
        byte[] fitxerBytes = fitxer.getContingut();

        try (FileOutputStream fileOutputStream = new FileOutputStream(DIR_ARRIBADA)) {
            fileOutputStream.write(fitxerBytes);
            System.out.println("Fitxer rebut guardat com: " + DIR_ARRIBADA);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connectar();
            System.out.println("Conexió acceptada: " + HOST + ":" + PORT);

            String nomFitxer;
            do {
                System.out.print("Nom del fitxer a rebre (\"sortir\" per sortir): ");
                nomFitxer = std.readLine();
                if(!nomFitxer.equalsIgnoreCase("sortir")) {
                    client.rebreFitxers(nomFitxer, client.in, client.out);
                }
            } while (!nomFitxer.equalsIgnoreCase("sortir"));
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
