import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClienXat extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean flag = false;

    public ClienXat(String name){
        super(name);
    }

    public void connecta(){
        try {
            socket = new Socket("localhost", 9999);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);
            System.out.println("Flux d'entrada i sortida creat.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void enviarMissatge(String message) {
        try {
            System.out.println("Enviant missatge: " + message);
            out.writeObject(message);
            out.flush(); 
        } catch (IOException e) {
            System.err.println("Error enviant missatge. Sortint...");
            flag = true;
        }
    }

    public void tancarClient() {
        try {
            out.close();
            in.close();
            socket.close();
            System.out.println("Flux d'entrada tancat.");
            System.out.println("Flux de sortida tancat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            System.out.println("DEBUG: Iniciant rebuda de missatges...");
            while (!flag) {
                String rawMessage = (String) in.readObject();
                if (rawMessage != null && !rawMessage.trim().isEmpty()) {
                    String code = Missatge.getCodiMissatge(rawMessage);
                    String[] codeParts = Missatge.getPartsMissatge(rawMessage);
                    switch (code) {
                        case Missatge.CODI_MSG_GRUP : {
                            String message = codeParts[1];
                            System.out.println("Missatge de grup: " + message);
                            break;
                        }
                        case Missatge.CODI_MSG_PERSONAL : {
                            String recipient = codeParts[1];
                            String message = codeParts[2];
                            System.out.println("Missatge personal de " + recipient + ": " + message);
                            break;
                        }
                        case Missatge.CODI_SORTIR_CLIENT : {
                            System.out.println("T'han desconnectat del servidor.");
                            flag = true;
                            break;
                        }
                        case Missatge.CODI_SORTIR_TOTS : {
                            System.out.println("Servidor tancat per a tots.");
                            flag = true;
                            break;
                        }
                        default : {
                            System.out.println("Missatge desconegut: " + rawMessage);
                        }
                    }
                }
            }
        } catch (Exception e) {
            if (!flag) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                flag = true;
            }  
        }
    }

    public String getLinea(BufferedReader std, String message, boolean obligatori) {
    if (message == null || message.isBlank()) return "";
    String line = "";
    
    do {
        System.out.print(message + " ");
        try {
            line = std.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "";
        }
    } while (obligatori && line.isEmpty());
    
    return line;
}

    public void ajuda() { System.out.printf("---------------------\nComandes disponibles:\n1.- Conectar al servidor (primer pas obligatori)\n2.- Enviar missatge personal\n3.- Enviar missatge al grup\n4.- (o línia en blanc) -> Sortir del client\n5.- Finalitzar tothom\n---------------------\n"); }

     public static void main(String[] args) {
        ClienXat client = new ClienXat("client");
        BufferedReader std = new BufferedReader(new InputStreamReader(System.in));

        try {
            client.connecta();
            client.start();

            while (!client.flag) {
                client.ajuda();
                System.out.print("> ");
                String option = std.readLine().trim();

                switch (option) {
                    case "":
                        client.setFlag(true);
                        break;
                    case "1": {
                        String name = client.getLinea(std, "Introdueix el nom:", true);
                        String message = Missatge.getMissatgeConectar(name);
                        client.enviarMissatge(message);
                        break;
                    }
                    case "2": {
                        String recipient = client.getLinea(std, "Destinatari:", true);
                        String message = client.getLinea(std, "Missatge a enviar:", true);
                        client.enviarMissatge(Missatge.getMissatgePersonal(recipient, message));
                        break;
                    }
                    case "3": {
                        String message = client.getLinea(std, "Missatge al grup:", true);
                        client.enviarMissatge(Missatge.getMissatgeGrup(message));
                        break;
                    }
                    case "4": {
                        client.enviarMissatge(Missatge.getMissatgeSortirClient("Adéu"));
                        client.setFlag(true);
                        break;
                    }
                    case "5": {
                        client.enviarMissatge(Missatge.getMissatgeSortirTots("Adéu"));
                        client.setFlag(true);
                        break;
                    }
                    default:
                        System.out.println("Opcio no valida: " + option);
                }
            }
            std.close();
            client.tancarClient();
        } catch (Exception e) {
            System.out.println("Error al connectar: " + e.getMessage());
            client.tancarClient();
        }    
    }
}
