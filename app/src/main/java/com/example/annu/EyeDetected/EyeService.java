package com.example.annu.EyeDetected;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import com.example.annu.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class EyeService extends Service {
    int count = 0;
    private Vibrator vibrator;
    MediaPlayer alarm;
    //SoundPool alarm;
   // int alarm_id;


    Timer my_timer = new Timer();
    TimerTask my_task = new TimerTask() {
        @Override
        public void run() {

            if (count < 3) {
                Log.e("timer count", "count :" + count);
               // alarm.stop(alarm_id);

                count++;

                if(alarm.isPlaying() == true)
                    alarm.stop();

            } else {


                vibrator.vibrate(1000); // 1초간 진동
                alarm.start();
               // alarm.play(alarm_id, 1, 1, 0, 0, 1);
                Log.e("time out", "time out");
                // my_timer.cancel();


            }
        }
    };
    CameraSource cameraSource;

    public EyeService() {
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }

        my_timer.cancel();
       // alarm.stop(alarm_id);
        alarm.stop();
        alarm.release();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // alarm_id = alarm.load(getApplicationContext(), R.raw.beep, 1);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        alarm = MediaPlayer.create(this,R.raw.beep);
        createCameraSource();//카메라 소스를 생성하며 아이트레킹 시작
        my_timer.schedule(my_task, 1000, 2000);
        return super.onStartCommand(intent, flags, startId);


    }


    public class EyesTracker extends Tracker<Face> {

        private final float THRESHOLD = 0.75f; // 눈을 뜬 정도 0.75 기준치 1~0

        private EyesTracker() {

        }


        @Override
        public void onMissing(Detector.Detections<Face> detections) { // 감지가 안될때 계속 돌지 않고 멈춘다

            Log.e("lost eye", "lost eye");
            //my_timer.schedule(my_task,5000);

        }

        @Override
        public void onUpdate(Detector.Detections<Face> detections, Face face) {//눈 감지 메서드
            if (face.getIsLeftEyeOpenProbability() > THRESHOLD || face.getIsRightEyeOpenProbability() > THRESHOLD)// 눈이 감지가 됐을때 기준치 THRESHOLD
            {
                Log.e("eye open", "eye open");
                count = 0;
            } else if (face.getIsLeftEyeOpenProbability() < THRESHOLD || face.getIsRightEyeOpenProbability() < THRESHOLD) {
                Log.e("eye close", "eye close");

            } else //눈이 감지 안될때
            {
                Log.e("no eye", "no eye");

            }


        }


    }


    public class FaceTrackerFactory implements MultiProcessor.Factory<Face> {
        private FaceTrackerFactory() {
        }

        @Override
        public Tracker<Face> create(Face face) {
            return new EyesTracker();
        }
    }

    public void createCameraSource() {
        FaceDetector detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        detector.setProcessor(new MultiProcessor.Builder(new FaceTrackerFactory()).build());

        cameraSource = new CameraSource.Builder(this, detector)
                .setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            cameraSource.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}




