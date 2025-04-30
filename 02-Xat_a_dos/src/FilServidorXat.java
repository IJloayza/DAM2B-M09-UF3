import java.io.ObjectInputStream;

public class FilServidorXat extends Thread {
    private ObjectInputStream in;
    
    public FilServidorXat(ObjectInputStream in, String nom) {
        super(nom);
        this.in = in;
        System.out.println("Fil de xat creat.");
        System.out.println("Fil de "+nom+" iniciat.");
    }

    @Override
    public void run() {
        while(in != null) {
            try {
                String missatge = (String) in.readObject();
                System.out.println("Missatge ('sortir' per tancar): Rebut: " + missatge);
                if (missatge.equalsIgnoreCase(ServidorXat.MSG_SORTIR)) {
                    System.out.println("Fil de xat finalitzat.");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
