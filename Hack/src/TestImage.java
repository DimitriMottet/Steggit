import java.io.File;

public class TestImage {
	public static void main(String[] argv){
		
		Image img1 = new Image("duck.bmp");
		img1.addText("love");
		File f1 = img1.getFile("duck2.bmp");
		
		Image img1test = new Image("duck2.bmp");
		String text = img1test.getText();
		System.out.println(text);
		
	}
}
