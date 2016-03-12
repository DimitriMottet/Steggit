import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;

public class Text 
{

	public static void main(String[] args) throws Exception
	{
		byte[] encVal = encrypt("Hello, world", "thisisasecretkey");
		System.out.println(encVal);
		
		System.out.println(decrypt(encVal, "thisisasecretkey") );
		
		System.out.println(getFileExtension("test.poauh"));
		
	}
	

	public static byte[] encrypt(String valueToEnc, String stringKey) {
	    Key key;
		try {
			key = generateKey(stringKey);	    
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encValue = c.doFinal(valueToEnc.getBytes());
			String encryptedValue = new String(encValue, "ASCII");
			return encValue;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public static String decrypt(byte[] encryptedValue, String stringKey){
	    Key key;
		try {
			key = generateKey(stringKey);	    
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = encryptedValue;
	    	byte[] decValue = c.doFinal(decordedValue);
	    	String decryptedValue = new String(decValue);
		    return decryptedValue;
	    }
	    catch(javax.crypto.BadPaddingException e)
	    {
	    	try {
				throw new Exception("Incorrect Key");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}
	    }catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		return null;
	    }
	    
	    
	}

	private static Key generateKey(String stringKey) throws Exception 
	{
		byte[] keyValue = stringKey.getBytes();
	    Key key = new SecretKeySpec(keyValue, "AES");
	    return key;
	}
	
	public static String getFileExtension(String fileName){
		String ext = "";
		int mode = 0;
		for(int i = 0; i < fileName.length(); i ++){
			if(mode == 0 && fileName.charAt(i) == '.'){
				mode = 1;
			}
			else if (mode == 1){
				ext += fileName.charAt(i);
			}	
		}
		return ext;
	}
	
}
