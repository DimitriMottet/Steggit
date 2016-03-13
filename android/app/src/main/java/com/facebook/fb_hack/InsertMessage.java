package com.facebook.fb_hack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class InsertMessage extends AppCompatActivity {

    private Uri loadedImageUri;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_message);

        // Retrieve the image
        Intent intent = getIntent();
        loadedImageUri = intent.getParcelableExtra("imageUri");
        if (loadedImageUri != null) {
            // Show the image in the imageView control and store it for later use
            ImageView imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageURI(loadedImageUri);
        }

        final Button saveButton = (Button) findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                encodeMessageAndSave();

            }
        });

        final Button shareButton = (Button) findViewById(R.id.buttonShare);
        shareButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                encodeMessageAndShare();
            }
        });

    }

    private void encodeMessageAndSave() {
        final Context context = this.getApplicationContext();
        final EditText editMessage = (EditText) findViewById(R.id.editMessage);
        final EditText editPassphrase = (EditText) findViewById(R.id.editPassphrase);
        if (editMessage == null || editMessage.getText() == null || editMessage.getText().toString().isEmpty()) {
            Toast.makeText(context, "A text is required", Toast.LENGTH_SHORT).show();
        } else if (editPassphrase == null || editPassphrase.getText() == null || editPassphrase.getText().toString().isEmpty()) {
            Toast.makeText(context, "A passphrase is required", Toast.LENGTH_SHORT).show();
        } else {
            progress = ProgressDialog.show(InsertMessage.this, "Stegging it", "Please wait...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String message = editMessage.getText().toString();
                    String passphrase = editPassphrase.getText().toString();
                    String filename = FileUtils.getPath(InsertMessage.this, loadedImageUri);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDither = false;
                    options.inMutable = true;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inScaled = false;
                    Image img = new Image(BitmapFactory.decodeFile(filename, options));

                    byte[] encodedText = TextEncryption.encrypt(message, passphrase);
                    img.addText(encodedText);
                    img.saveFile(addSuffix(filename, "_stegged"));
                    progress.dismiss();
                    InsertMessage.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(InsertMessage.this, "Saved to Pictures folder", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).start();
        }
    }

    // Launch share message activity
    private void encodeMessageAndShare() {
        final Context context = this.getApplicationContext();
        final EditText editMessage = (EditText) findViewById(R.id.editMessage);
        final EditText editPassphrase = (EditText) findViewById(R.id.editPassphrase);
        if (editMessage == null || editMessage.getText() == null || editMessage.getText().toString().isEmpty()) {
            Toast.makeText(context, "A text is required", Toast.LENGTH_SHORT).show();
        } else if (editPassphrase == null || editPassphrase.getText() == null || editPassphrase.getText().toString().isEmpty()) {
            Toast.makeText(context, "A passphrase is required", Toast.LENGTH_SHORT).show();
        } else {
            progress = ProgressDialog.show(InsertMessage.this, "Stegging it", "Please wait...", true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String message = editMessage.getText().toString();
                    String passphrase = editPassphrase.getText().toString();
                    String filename = FileUtils.getPath(InsertMessage.this, loadedImageUri);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDither = false;
                    options.inMutable = true;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inScaled = false;
                    Image img = new Image(BitmapFactory.decodeFile(filename, options));

                    byte[] encodedText = TextEncryption.encrypt(message, passphrase);
                    img.addText(encodedText);
                    String newFilename = addSuffix(filename, "_stegged");
                    img.saveFile(newFilename);
                    progress.dismiss();

                    // Open share menu
                    Intent shareIntent = new Intent();
                    Uri uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), newFilename));
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, "Share to..."));

                }
            }).start();
        }
    }

    private String addSuffix(String filename, String suffix) {
        return new String(filename.substring(filename.lastIndexOf('/') + 1, filename.lastIndexOf('.')) + suffix + ".png");
    }

}
