import java.io.File;


public class GeneralTest {
	
	public static void main (String[] argv){
	
	String s = "LOVE";
	String k = "ThisIsTheKeykkkk";
	
	
	// Encrypt Text
	byte encryptedB[] = Text.encrypt(s,k);
	System.out.println("Plain message: "+s);
	System.out.println("Encrypted message: "+encryptedB);
	
	
	// Add it to the photo
	System.out.println("Image processing...");
	
	Image img1 = new Image("duck.bmp");
	img1.addTextBytes(encryptedB);
	img1.getFile("duck2.bmp");
		
	// Get the text from the photo
	Image img1test = new Image("duck2.bmp");
	byte[] text = img1test.getTextBytes();
	System.out.println("Encrypted message: "+text);
	
	// Decrypt the text
	String decryptedS = Text.decrypt(text, k);
	System.out.println("Decrypted message: "+decryptedS);
	
	}
	
}
