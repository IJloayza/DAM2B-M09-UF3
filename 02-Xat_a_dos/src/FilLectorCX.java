import java.io.ObjectInputStream;

public class FilLectorCX extends Thread {
    private ObjectInputStream in = null;

    public FilLectorCX(ObjectInputStream in) {
        this.in = in;
        System.out.println("Fil de lectura creat.");
    }

    @Override
    public void run() {
        try {
            String missatge;
            while (!(missatge = (String) in.readObject()).equals(ServidorXat.MSG_SORTIR)) {
                System.out.println("Missatge ('sortir' per tancar):Rebut: " + missatge);
            }
        } catch (Exception e) {
            System.out.println("Error reptint el missatge des de Servidor.");
        }
    }
}
