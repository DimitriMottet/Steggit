package com.facebook.fb_hack;

import android.app.ProgressDialog;
import android.content.Context;
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

        final Button encodeButton = (Button) findViewById(R.id.buttonEncode);
        encodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                encodeMessageOnImage();
                startShare();
            }
        });
    }

    private void encodeMessageOnImage() {
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
                    String filename = getRealPathFromURI(loadedImageUri);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inDither = false;
                    options.inMutable = true;
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    options.inScaled = false;
                    Image img = new Image(BitmapFactory.decodeFile(filename, options));

                    byte[] encodedText = TextEncryption.encrypt(message, passphrase);
                    img.addText(encodedText);
                    progress.dismiss();

                    // Ask what to do with the file
                    img.saveFile("testfile.png");
                }
            }).start();
        }
    }

    // Launch share message activity
    private void startShare(){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, loadedImageUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Share to..."));
    }

    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage"))
            return contentUri.getPath();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
        String realPath = null;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            realPath = cursor.getString(columnIndex);
        }
        cursor.close();
        return realPath;
    }

}
