package pl.edu.agh.student.pracainz;

import org.opencv.android.JavaCameraView;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;

public class CustomizableCameraView extends JavaCameraView {

	private final static String TAG = "***** CUSTOMIZABLE CAMERA VIEW  *****";
	
	
    public CustomizableCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPreviewFPS(double min, double max){
    	
    	Log.d(TAG,"  00000000000000000000000000000      ");
    	Log.d(TAG,"  00000000000000000000000000000      ");
    
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewFpsRange((int)(min*1000), (int)(max*1000));
        mCamera.setParameters(params);
    }
}