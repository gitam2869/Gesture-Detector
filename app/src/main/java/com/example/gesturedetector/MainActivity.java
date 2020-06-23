package com.example.gesturedetector;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener
{
    private static final String TAG = "MainActivity";
    
    LinearLayout linearLayout;
    TextView textView;
    GestureDetector gestureDetector;
    GestureDetector gestureDetector1;

    int singleTapCount = 0;

//    Video Uploader

    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPhone;
    private TextView textViewSubject;

//    Drawer Layout
    DrawerLayout drawerLayout;

//    exoplayer

    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;
    private ProgressBar progressBar;

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private TextView textViewTopicName;
    private ImageButton imageButtonFullScreen;
    private ImageButton imageButtonFullScreenExit;
    private ImageButton imageButtonPlaybackSpeed;
    private ImageButton imageButtonAspectRatio;


    public PlaybackParameters playbackParameters;
    public float videoSpeed = 1f;
    public int checkid;

    public int videoPosition = 0;
    public boolean videoPositionState = true;
    public int count = 0;

    public String videoLink;

//    video list

    ArrayList<VideoInfo> videoInfoArrayList;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

//        final View decorView = getWindow().getDecorView();

//        decorView.setOnSystemUiVisibilityChangeListener
//                (new View.OnSystemUiVisibilityChangeListener() {
//                    @Override
//                    public void onSystemUiVisibilityChange(int visibility) {
//                        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
//                            // TODO: The navigation bar is visible. Make any desired
//                            // adjustments to your UI, such as showing the action bar or
//                            // other navigational controls.
//                            hideNavigationBar();
//
//                        } else {
//                            // TODO: The navigation bar is NOT visible. Make any desired
//                            // adjustments to your UI, such as hiding the action bar or
//                            // other navigational controls.
//                        }
//                    }
//                });

//        video uploader

        textViewName = findViewById(R.id.idTextViewName);
        textViewEmail = findViewById(R.id.idTextViewEmail);
        textViewPhone = findViewById(R.id.idTextViewPhone);
        textViewSubject = findViewById(R.id.idTextViewSubject);

        playerView = findViewById(R.id.idPlayerViewSubscribedCourseActivity);

        linearLayout = findViewById(R.id.idRelativeLayoutMainActivity);

        videoInfoArrayList = new ArrayList<>();

        drawerLayout = findViewById(R.id.drawerLayout);

        drawerLayout.setScrimColor(Color.TRANSPARENT);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                0,
                0

        )
        {
            private float scaleFactor = 6f;
            /**
             * {@link DrawerLayout.DrawerListener} callback method. If you do not use your
             * ActionBarDrawerToggle instance directly as your DrawerLayout's listener, you should call
             * through to this method from your own listener object.
             *
             * @param drawerView  The child view that was moved
             * @param slideOffset The new offset of this drawer within its range, from 0-1
             */
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);

                float slideX = drawerView.getWidth() * slideOffset;
                linearLayout.setTranslationX(slideX);

                linearLayout.setScaleX(1 - (slideOffset / scaleFactor));
                linearLayout.setScaleY(1 - (slideOffset / scaleFactor));
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);


//        exoplayerview

        progressBar = findViewById(R.id.idProgressbarMainSubscribedCourseActivity);
        hideSystemUi();

        textView = findViewById(R.id.textview);
        textViewTopicName = findViewById(R.id.exo_textview_topicname);
        imageButtonFullScreen = findViewById(R.id.exo_fullscreen_button);
        imageButtonFullScreenExit = findViewById(R.id.exo_fullscreen_exit_button);
        imageButtonPlaybackSpeed = findViewById(R.id.exo_playback_speed_setting);
        imageButtonAspectRatio = findViewById(R.id.exo_aspect_ration_settings);


        checkid = R.id.Radio2;

        imageButtonPlaybackSpeed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertMessage();
            }
        });

        imageButtonFullScreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageButtonFullScreen.setVisibility(View.GONE);
                imageButtonFullScreenExit.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            }
        });

        imageButtonFullScreenExit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                imageButtonFullScreen.setVisibility(View.VISIBLE);
                imageButtonFullScreenExit.setVisibility(View.GONE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

            }
        });

        imageButtonAspectRatio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                count++;

                Log.d("TAG", "onClick1111: "+count);

                if(count == 0)
                {
                    Log.d("TAG", "onClick0: "+count);

                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                }
                else if(count == 1)
                {
                    Log.d("TAG", "onClick1: "+count);

                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
                }
                else if(count == 2)
                {
                    Log.d("TAG", "onClick2: "+count);

                    playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                    count = -1;
                }
            }
        });

        gestureDetector = new GestureDetector(this, new OnSwipeListener()
        {
            /**
             * Override this method. The Direction enum will tell you how the user swiped.
             *
             * @param direction
             */
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("WrongConstant")
            @Override
            public boolean onSwipe(Direction direction)
            {
                if(direction == Direction.down)
                {

//                    decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
//                    {
//                        @Override
//                        public void onSystemUiVisibilityChange(int visibility)
//                        {
//                            Log.d(TAG, "onSystemUiVisibilityChange: down "+visibility);
//
//                            if ((visibility & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == 0)
//                            {
//                                // TODO: The navigation bar is visible. Make any desired
//                                // adjustments to your UI, such as showing the action bar or
//                                // other navigational controls.
////                                        hideNavigationBar();
//                                Toast.makeText(getApplicationContext(), "Status", Toast.LENGTH_SHORT).show();
//
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(), "No Status", Toast.LENGTH_SHORT).show();
//
//                                if(videoPosition != 0)
//                                {
//                                    videoPosition--;
//                                }
//                                else
//                                {
//                                    videoPosition = videoInfoArrayList.size()-1;
//                                }
//                                // TODO: The navigation bar is NOT visible. Make any desired
//                                // adjustments to your UI, such as hiding the action bar or
//                                // other navigational controls.
//                            }
//                        }
//                    });


                    if(videoPosition != 0)
                    {
                        videoPosition--;
                    }
                    else
                    {
                        videoPosition = videoInfoArrayList.size()-1;
                    }

                    SharedPreferenceManager.getInstance(getApplicationContext()).videoNumber(videoPosition);

                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(R.anim.bottom_down,R.anim.nothing);

//                    Toast.makeText(getApplicationContext(), "down", Toast.LENGTH_SHORT).show();
                }
                else if(direction == Direction.up)
                {

//                    decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
//                    {
//                        @Override
//                        public void onSystemUiVisibilityChange(int visibility)
//                        {
//                            Log.d(TAG, "onSystemUiVisibilityChange: up "+visibility);
//
//                            if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0)
//                            {
//                                // TODO: The navigation bar is visible. Make any desired
//                                // adjustments to your UI, such as showing the action bar or
//                                // other navigational controls.
////                                        hideNavigationBar();
//
//                            }
//                            else
//                            {
//                                if(videoPosition != videoInfoArrayList.size() - 1)
//                                {
//                                    videoPosition++;
//                                }
//                                else
//                                {
//                                    videoPosition = 0;
//                                }
//                                // TODO: The navigation bar is NOT visible. Make any desired
//                                // adjustments to your UI, such as hiding the action bar or
//                                // other navigational controls.
//                            }
//                        }
//                    });

                    if(videoPosition != videoInfoArrayList.size() - 1)
                    {
                        videoPosition++;
                    }
                    else
                    {
                        videoPosition = 0;
                    }

                    SharedPreferenceManager.getInstance(getApplicationContext()).videoNumber(videoPosition);

                    finish();
                    startActivity(getIntent());
                    overridePendingTransition(R.anim.bottom_up,R.anim.nothing);

//                    Toast.makeText(getApplicationContext(), "up", Toast.LENGTH_SHORT).show();
                }
                else if(direction == Direction.left)
                {

                    subscribeCreator();
//                    Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    drawerLayout.openDrawer(GravityCompat.START);

//                    Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
                }

                return super.onSwipe(direction);
            }
        });

        gestureDetector1 = new GestureDetector(this, new OnSwipeListener()
        {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e)
            {
                playerView.setUseController(true);

                return super.onSingleTapConfirmed(e);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: textvie");
                playerView.setUseController(true);
            }
        });
//        linearLayout.setOnTouchListener(this);
//        playerView.setOnTouchListener(this);
        
        linearLayout.setOnTouchListener(new View.OnTouchListener() 
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        playerView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
//                Log.d(TAG, "onTouch: "+event);

                gestureDetector.onTouchEvent(event);
                return false;
            }
        });

//        drawerLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                gestureDetector.onTouchEvent(event);
//                return false;
//            }
//        });

        if(!isConnected())
        {

            setContentView(R.layout.main_no_internet);
//            getSupportActionBar().hide();
            alertMessageNetwork();
            return;

        }

        getVideo();

    }

    private void hideNavigationBar() {

        Toast.makeText(getApplicationContext(), "hideNavigationBar()", Toast.LENGTH_SHORT).show();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        gestureDetector.onTouchEvent(event);

        if(v == playerView)
        {
            if(singleTapCount == 0)
            {
                playerView.setUseController(true);
                singleTapCount++;
            }
            else
            {
                playerView.setUseController(false);
                singleTapCount--;
            }
            gestureDetector1.onTouchEvent(event);
        }
//            playerView.setUseController(true);

        return true;
    }

    private void getVideo()
    {
        videoInfoArrayList.add(new VideoInfo(1, "Maths", "http://agro-checkmake.000webhostapp.com/ceed/videos/coursevideo/VID_Banking_SBI%20PO_General%20Information_Ham_13_Jun_2020_07:41:26.mp4", "Gautam1", "gautam1@gmail.com", "9988776655"));
        videoInfoArrayList.add(new VideoInfo(2, "Physics", "http://agro-checkmake.000webhostapp.com/ceed/videos/dummyVideos/video4.mp4", "Gautam2", "gautam2@gmail.com", "9988776644"));
        videoInfoArrayList.add(new VideoInfo(3, "English", "http://agro-checkmake.000webhostapp.com/ceed/videos/dummyVideos/VID-20200307-WA0030.mp4", "Gautam3", "gautam3@gmail.com", "9988776633"));
        videoInfoArrayList.add(new VideoInfo(4, "Grammar", "http://agro-checkmake.000webhostapp.com/ceed/videos/dummyVideos/Video_20200613125203289_by_videoshow.mp4", "Gautam4", "gautam4@gmail.com", "9988776622"));
        videoInfoArrayList.add(new VideoInfo(5, "History", "http://agro-checkmake.000webhostapp.com/ceed/videos/dummyVideos/VID-20200306-WA0007.mp4", "Gautam5", "gautam5@gmail.com", "9988776611"));

//        videoLink = "http://agro-checkmake.000webhostapp.com/ceed/videos/dummyVideos/VID-20200307-WA0030.mp4";
        initializePlayer();

    }

    public void alertMessage()
    {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.custom_dialog_playback_speed_settings, null);

        final RadioGroup radioGroup = view.findViewById(R.id.RadioGroup);
        TextView textViewClose = view.findViewById(R.id.textview1);

        builder.setView(view);

        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        final RadioButton rb =  radioGroup.findViewById(checkid);
        radioGroup.check(checkid);

        rb.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(rb.getId() == checkid)
                {
                    alertDialog.dismiss();
                    hideSystemUi();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Log.d("TAG", "onCheckedChanged0: ");
                RadioButton rb =  radioGroup.findViewById(checkedId);
                boolean checked = rb.isChecked();

                Log.d("TAG", "onCheckedChanged1: "+rb.getId());
                Log.d("TAG", "onCheckedChanged2: "+checkedId);


                switch (rb.getId())
                {
                    case R.id.Radio1:
                        if (checked)
                        {
                            alertDialog.dismiss();
                            checkid = checkedId;
                            videoSpeed = 0.5f;
                        }

                        break;

                    case R.id.Radio2:
                        if (checked)
                        {
                            alertDialog.dismiss();
                            checkid = checkedId;
                            videoSpeed = 1f;
                        }
                        break;

                    case R.id.Radio3:
                        if (checked)
                        {
                            alertDialog.dismiss();
                            checkid = checkedId;
                            videoSpeed = 1.5f;
                        }
                        break;

                    case R.id.Radio4:
                        if (checked)
                        {
                            alertDialog.dismiss();
                            checkid = checkedId;
                            videoSpeed = 2f;
                        }
                        break;
                }

                hideSystemUi();
                playbackParameters = new PlaybackParameters(videoSpeed);
                simpleExoPlayer.setPlaybackParameters(playbackParameters);
            }
        });

        textViewClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
                hideSystemUi();
            }
        });

        alertDialog.show();

//        Log.d("TAG", "onCreate: *6"+demovideo);
//        Log.d("TAG", "alertMessage: "+videoSpeed);
    }

    private void initializePlayer()
    {
        if(videoPositionState)
        {
            int previousVideoPosition = SharedPreferenceManager.getInstance(this).getVideoNumber();

            if(previousVideoPosition == -1)
            {
                VideoInfo videoInfo = videoInfoArrayList.get(0);
                videoLink = videoInfo.getVideo();
                videoPosition = 0;
                int s = videoPosition + 1;
                textViewTopicName.setText(""+s+". "+videoInfo.getTopic());
            }
            else
            {
                VideoInfo videoInfo = videoInfoArrayList.get(previousVideoPosition);
                videoLink = videoInfo.getVideo();
                int s = previousVideoPosition+1;
                textViewTopicName.setText(""+s+". "+videoInfo.getTopic());
                videoPosition = previousVideoPosition;
            }

            videoPositionState = false;
        }

        VideoInfo videoInfo = videoInfoArrayList.get(videoPosition);

        textViewName.setText(videoInfo.getName());
        textViewEmail.setText(videoInfo.getEmail());
        textViewPhone.setText(videoInfo.getPhone());
        textViewSubject.setText(videoInfo.getTopic());

        if (simpleExoPlayer == null)
        {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
        }
        else
        {
            releasePlayer();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this);
            playbackPosition = 0;
        }


        playerView.setPlayer(simpleExoPlayer);

        Uri uri = Uri.parse(videoLink);

        playbackParameters = new PlaybackParameters(videoSpeed);
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(this, "Spinner"));

//        MediaSource mediaSource = buildMediaSource(uri);

        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);;


        simpleExoPlayer.setPlayWhenReady(playWhenReady);
        simpleExoPlayer.seekTo(currentWindow, playbackPosition);
        simpleExoPlayer.prepare(mediaSource, false, false);
        simpleExoPlayer.setPlaybackParameters(playbackParameters);

        progressBar.setVisibility(View.GONE);

//        Log.d("TAG", "onCreate: *5"+demovideo);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    private void subscribeCreator()
    {


        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = getLayoutInflater().inflate(R.layout.custom_dialog_subscribe_creator, null);

//        views
        TextView textViewTopicName = view.findViewById(R.id.idTextviewTopicName);
        final MaterialButton materialButtonSubmit = view.findViewById(R.id.idButtonSubmitNewExam);
        TextView textViewCancel = view.findViewById(R.id.idTextViewCancelNewExam);

        builder.setView(view);

        builder.setCancelable(false);

        final AlertDialog alertDialog = builder.create();

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        VideoInfo videoInfo = videoInfoArrayList.get(videoPosition);
        textViewTopicName.setText(videoInfo.getTopic());

        textViewCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });

        materialButtonSubmit.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v)
            {
                materialButtonSubmit.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
                materialButtonSubmit.setText("Subscribed");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                    }
                }, 2000);


            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 5000);

        alertDialog.show();

    }


    private void releasePlayer()
    {
        hideSystemUi();
        if (simpleExoPlayer != null)
        {
            playbackPosition = simpleExoPlayer.getCurrentPosition();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            currentWindow = simpleExoPlayer.getCurrentWindowIndex();
            simpleExoPlayer.release();
            simpleExoPlayer = null;

        }
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        hideSystemUi();

        if ((Util.SDK_INT < 24 || simpleExoPlayer == null))
        {
            Log.d("TAG", "onRestart: 1111");
            initializePlayer();
        }
    }

    @Override
    public void onPause()
    {
        hideSystemUi();

        super.onPause();

        if (Util.SDK_INT < 24)
        {
            releasePlayer();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        hideSystemUi();

        if (Util.SDK_INT >= 24)
        {
            releasePlayer();
        }

//        SharedPreferenceManager.getInstance(getApplicationContext())
//                .videoNumber(videoPosition);
    }



    @SuppressLint("InlinedApi")
    private void hideSystemUi()
    {
        playerView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onBackPressed()
    {

        Log.d("TAG", "onBackPressed: 1");
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            imageButtonFullScreen.setVisibility(View.VISIBLE);
            imageButtonFullScreenExit.setVisibility(View.GONE);
            Log.d("TAG", "onBackPressed: 2");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            Log.d("TAG", "onBackPressed: 3");
            // In landscape
        }
        else
        {
            super.onBackPressed();
            overridePendingTransition(0,0);
//            mkPlayer.pause();
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        Log.d("TAG", "onConfigurationChanged: *");


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            imageButtonFullScreen.setVisibility(View.GONE);
            imageButtonFullScreenExit.setVisibility(View.VISIBLE);

        }
        else
        {
            imageButtonFullScreen.setVisibility(View.VISIBLE);
            imageButtonFullScreenExit.setVisibility(View.GONE);
        }
    }


    public boolean isConnected()
    {
        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
        {
            //we are connected to a network
            connected = true;
        }
        else
        {
            connected = false;
        }

        return connected;
    }


    public void alertMessageNetwork()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Please connect with to working internet connection");

        // Set Alert Title
        builder.setTitle("Network Error!");

        // Set Cancelable false// Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.

        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Ok",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                finish();
                                startActivity(getIntent());
                            }
                        });


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

}