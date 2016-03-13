import java.io.File;


public class GeneralTest {
	
	public static void main (String[] argv){
	
	String s = "LOVE";
	String k = "ThisIsTheKeykkkk";
	
	
	// Encrypt Text
	byte encryptedB[] = Text.encrypt(s,k);
	String encryptedS = bytesToHex(encryptedB);
	//String encryptedS = new String(encryptedB);
	System.out.println("Plain message: "+s);
	System.out.println("Encrypted message: "+encryptedS);
	
	
	// Add it to the photo
	System.out.println("Image processing...");
	
	Image img1 = new Image("duck.bmp");
	img1.addText(encryptedS);
	img1.getFile("duck2.bmp");
		
	// Get the text from the photo
	Image img1test = new Image("duck2.bmp");
	String text = img1test.getText();
	System.out.println("Encrypted message: "+text);
	
	// Decrypt the text
	String decryptedS = Text.decrypt(text.getBytes(), k);
	System.out.println("Decrypted message: "+decryptedS);
	
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
}
