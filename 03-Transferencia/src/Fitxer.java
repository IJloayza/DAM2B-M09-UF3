import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Fitxer {
    private String nom;
    byte[] contingut;
    
    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() {
        try {
            File file = new File(nom);
            if(!file.exists()) {
                throw new Exception("El fitxer no existeix");
            }else{
                Path path = file.toPath();
                contingut = Files.readAllBytes(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return contingut;
    }
}
