package br.com.obatag.ipix.client;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.first.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	// Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    
    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    
    private Uri fileUri; // file url to store image/video
    
    private ImageView imgPreview;
    private Button btnCapturePicture;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				captureImage();
			}
		});
    }
    
    public void captureImage() {
		Intent intCapturePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
		intCapturePic.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intCapturePic, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
    		if(resultCode == RESULT_OK){
    			previewCaptureImage();
    		}else if(resultCode == RESULT_CANCELED){
    			Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
    		}else{
    			Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
    		}
    	}
    }
    public void previewCaptureImage(){
    	try{
    		imgPreview.setVisibility(View.VISIBLE);
    		BitmapFactory.Options options = new BitmapFactory.Options();
    		options.inSampleSize = 8;
    		final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
    		imgPreview.setImageBitmap(bitmap);
    		Log.i ("info", fileUri.getPath());
    		
    	}catch(NullPointerException e){
    		e.printStackTrace();
    	}
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
    
    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
     
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }
     
    /*
     * Here we restore the fileUri again
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
     
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }
}







