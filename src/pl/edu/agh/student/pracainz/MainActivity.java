package pl.edu.agh.student.pracainz;

import java.io.IOException;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import pl.edu.agh.student.pracainz.filters.Filter;
import pl.edu.agh.student.pracainz.filters.NoneFilter;
import pl.edu.agh.student.pracainz.filters.ar.ImageDetectionFilter;
import android.support.v7.app.ActionBarActivity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements CvCameraViewListener2{

	
	private ImageView image;
	
	 protected Camera mCamera;
	
	private final static String TAG = "***** MainActivity *****";
	
	private boolean mIsTakePhoto = false;
	
	//a matrix that is used when saving the photo
	private Mat mBgr;
	
	// Keys for storing the indices of the active filters
	private static final String STATE_IMAGE_DETECTION_FILTER_INDEX = "imageDetectionFilterIndex";
		
	//Keys for storing the indices of the active filters
	private static final String STATE_CURVE_FILTER_INDEX = "curveFilterIndex";
	
	private static final String STATE_MIXER_FILTER_INDEX = "mixerFilterIndex";
	
	private static final String STATE_CONVOLUTION_FILTER_INDEX = "convolutionFilterIndex";
	
	// the filters	
	private Filter[] mImageDetectionFilters;
	
	// The indices of the active filters	
	private int mImageDetectionFilterIndex;
	
	private JavaCameraView mOpenCvCameraView;
	
	
	// private JavaCameraView mOpen1;
	
	// whether an aynchronous menu action is in progress
	// if so, menu interaction should be disabled	
	private boolean mIsMenuLocked;
	
	// The OpenCV loader callback
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(final int status){
			switch(status){
				case LoaderCallbackInterface.SUCCESS:					
					Log.d(TAG, "openCV loaded successfully");
					mOpenCvCameraView.enableView();					
					mBgr = new Mat();
					
					final Filter starryNight;
					try{
						starryNight = new ImageDetectionFilter(MainActivity.this, R.drawable.starry_night);
						Log.e( TAG, "##########  Success to load strry night    ############## ");
					}
					catch(IOException e){
						Log.e(TAG, "########################################");
						Log.e(TAG, "$$$$$$$$$" + "failed to load drawable: " + "starry_night" + "$$$$$$$$$$$$$$$$$");
						Log.e(TAG, "########################################");
						e.printStackTrace();
						break;
					}														
					
					final Filter akbarHunting;
                   try {
                       akbarHunting = new ImageDetectionFilter(
                               MainActivity.this,
                               R.drawable.akbar_hunting_with_cheetahs);
                       Log.e( TAG, "##########  Success to load akbar hunting   ############## ");
                   } catch (IOException e) {
                   	Log.e(TAG, "########################################");
                       Log.e(TAG, "Failed to load drawable: " +
                               "akbar_hunting_with_cheetahs");
                       Log.e(TAG, "########################################");
                       e.printStackTrace();
                       break;
                   }
                   
                   final Filter mariac_1;
                   try {
                       mariac_1 = new ImageDetectionFilter(
                               MainActivity.this,
                               R.drawable.mariac_1);
                       Log.e( TAG, "##########  Success to load akbar hunting   ############## ");
                   } catch (IOException e) {
                   	Log.e(TAG, "########################################");
                       Log.e(TAG, "Failed to load drawable: " +
                               "akbar_hunting_with_cheetahs");
                       Log.e(TAG, "########################################");
                       e.printStackTrace();
                       break;
                   }
                   
					mImageDetectionFilters = new Filter[]{
					 	   new NoneFilter(),  starryNight , akbarHunting  , mariac_1
					};												
														
					break;				
				default:				
					super.onManagerConnected(status);
					break;
				
			}
		}
	};	
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
			
//  trzeba by to dac do nowej klasy z tym, ze zdjecie powinno byc wybierane dynamicznie i do niego przezroczystosc
		image = (ImageView) findViewById(R.id.imageView1);
		image.setAlpha(127);
		
		image.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int eid = event.getAction();
                switch (eid) {
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) image.getLayoutParams();
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();
                    mParams.leftMargin = x-50;
                    mParams.topMargin = y-50;
                    image.setLayoutParams(mParams);
                    break;

                default:
                    break;
                }
                return true;
            }
        });
		
						
		if ( savedInstanceState != null) {
			mImageDetectionFilterIndex = savedInstanceState.getInt(
					STATE_IMAGE_DETECTION_FILTER_INDEX, 0);					
		}
		else {
			mImageDetectionFilterIndex = 0;				
		}
		
		mOpenCvCameraView = (JavaCameraView) findViewById(R.id.MainActivityCameraView);		
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);	
				
	}

	@Override
	public void onPause(){
		if ( mOpenCvCameraView != null){
			mOpenCvCameraView.disableView();
		}
		super.onPause();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this , mLoaderCallback);
		mIsMenuLocked = false;
	}
	
	public void onDestroy(){
		super.onDestroy();
		if ( mOpenCvCameraView != null) {
			mOpenCvCameraView.disableView();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(final MenuItem item){			
		
	switch ( item.getItemId()){	
	case R.id.menu_next_image_detection_filter:							
		mImageDetectionFilterIndex++;						
		if (mImageDetectionFilterIndex == mImageDetectionFilters.length ){
			mImageDetectionFilterIndex = 0;
		}
		return true;	
		
	case R.id.menu_change_fps:		
		Log.e( TAG, "   0000000000000000000000000000000000        ");
		mOpenCvCameraView = (JavaCameraView) findViewById(R.id.slow_motion);		
		mOpenCvCameraView.setCvCameraViewListener(this);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE); 
				
		default:	 
			return super.onOptionsItemSelected(item);			
	    }	
	}
	
	

	
	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {		
		final Mat rgba = inputFrame.rgba();					
		
		// Apply the active filters
		if ( mImageDetectionFilters != null) {
			Log.d(TAG,"**** mImageDetectionFilters is different than null ****");
			mImageDetectionFilters[mImageDetectionFilterIndex].apply(rgba, rgba);			
		}		
		return rgba;			 
	}
}
