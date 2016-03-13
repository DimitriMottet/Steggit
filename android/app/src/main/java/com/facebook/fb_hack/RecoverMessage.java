package com.facebook.fb_hack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class RecoverMessage extends AppCompatActivity {

    private Uri loadedImageUri;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_message);

        // Retrieve the image
        Intent intent = getIntent();
        loadedImageUri = intent.getParcelableExtra("imageUri");
        if (loadedImageUri != null) {
            // Show the image in the imageView control and store it for later use
            ImageView imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageURI(loadedImageUri);
        }

        final Button encodeButton = (Button) findViewById(R.id.buttonRecover);
        encodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recoverMessageFromImage();
            }
        });
    }

    private void recoverMessageFromImage() {
        final EditText editPassphrase = (EditText) findViewById(R.id.editPassphrase);
        final EditText editPlainText = (EditText) findViewById(R.id.editPlainText);
        if (editPassphrase == null || editPassphrase.getText() == null || editPassphrase.getText().toString().isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "A passphrase is required", Toast.LENGTH_SHORT).show();
        } else {
            progress = ProgressDialog.show(RecoverMessage.this, "De-stegging it", "Please wait...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String passphrase = editPassphrase.getText().toString();
                    String filename = FileUtils.getPath(RecoverMessage.this, loadedImageUri);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDither = false;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inScaled = false;
                    Image img = new Image(BitmapFactory.decodeFile(filename, options));

                    byte[] encodedText = img.getText();
                    if (encodedText != null) {
                        final String plaintext = TextEncryption.decrypt(encodedText, passphrase);
                        editPlainText.post(new Runnable() {
                            @Override
                            public void run() {
                                editPlainText.setText(plaintext);
                            }
                        });
                    }
                    progress.dismiss();
                }
            }).start();
        }
    }

}
