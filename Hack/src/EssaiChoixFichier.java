import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;

class EssaiChoixFichier {
	public static void main(String[] arg) throws IOException {
		JFileChooser dialogue = new JFileChooser(new File("."));
		File fichier;
		
		if (dialogue.showOpenDialog(null)== 
		    JFileChooser.APPROVE_OPTION) {
		    fichier = dialogue.getSelectedFile();
		    System.out.println(fichier.getName());
		}
    }
}