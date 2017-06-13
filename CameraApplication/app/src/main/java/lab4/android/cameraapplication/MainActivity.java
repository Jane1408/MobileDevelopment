package lab4.android.cameraapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Uri mOutputFileUri;
    private static int TAKE_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("CamPro");
        mImageView = (ImageView) findViewById(R.id.imageView2);
    }

    public void onClick(View v) {
        try {
            saveFullImage();
        } catch (ActivityNotFoundException e) {
            String errorMessage = "Camera not found";
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            setPictureToImageView();
            galleryAddPicture();
        }
    }

    private void setPictureToImageView() {
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mOutputFileUri.getPath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(mOutputFileUri.getPath(), bmOptions);
        mImageView.setImageBitmap(bitmap);
    }

    private void saveFullImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = String.valueOf(System.currentTimeMillis());
        File file = new File(Environment.getExternalStorageDirectory(), timeStamp + "image.jpg");
        mOutputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private void galleryAddPicture() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(mOutputFileUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
