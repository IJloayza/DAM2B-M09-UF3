import java.io.BufferedReader;
import java.io.File;
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

        byte[] contingut = (byte[]) in.readObject();

        String rutaDesti = DIR_ARRIBADA + File.separator + new File(nomFitxer).getName();
        try (FileOutputStream dirRebut = new FileOutputStream(rutaDesti)) {
            dirRebut.write(contingut);
        }
        System.out.println("Fitxer rebut guardat com: " + rutaDesti);
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
                }else{
                    System.out.println("Sortint...");
                }
            } while (!nomFitxer.equalsIgnoreCase("sortir"));
            System.out.println("Conexió Tancada.");
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
