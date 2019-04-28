package com.example.annu.EyeDetected;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Timer;
import java.util.TimerTask;

import com.example.annu.MainActivity;
import com.example.annu.R;
import com.example.annu.Study;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public class EyeService extends Service {
    int count = 0;//공용으로 사용하는 count 변수 눈이 인식되면 초기화 된다
    private Vibrator vibrator;
    MediaPlayer alarm;//경고음 출력 변수

    AudioManager volume;
    int basic_volume;

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    Timer my_timer = new Timer();
    TimerTask my_task = new TimerTask() {
        @Override
        public void run() {

            if (count < 3) {//정해진 시간동안 반복
                Log.e("timer count", "count :" + count);
                // alarm.stop(alarm_id);

                count++;//시간을 카운트함

                if (alarm.isPlaying() == true)
                    alarm.stop();//경고음 정지
                volume.setStreamVolume(AudioManager.STREAM_MUSIC, basic_volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);//기존 음량으로 변경
            } else {

                volume.setStreamVolume(AudioManager.STREAM_MUSIC,
                        volume.getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);//음량을 최대로 변경
                vibrator.vibrate(1000); // 1초간 진동
                alarm.start();//경고음 시작
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
    public void onDestroy() {//서비스가 종료될때
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }

        my_timer.cancel();//진동과 경고음 모두 해제
        alarm.stop();
        alarm.release();

        mNotificationManager.cancel(3452);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        alarm = MediaPlayer.create(this, R.raw.beep);
        volume = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        basic_volume = volume.getStreamVolume(AudioManager.STREAM_MUSIC);//음량을 최대로 하기 전 기존 음량을 가져온다


/////////////////////////////상단바////////////////////////


        NotificationManager notificationManager
                = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {//NotificationCompat 버젼문제로 인한 버젼검사
            NotificationChannel mChannel = new NotificationChannel("channel", "Name",
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);

            mBuilder = new NotificationCompat.Builder(this, "channel")
                    .setSmallIcon(R.drawable.bee)
                    .setContentTitle("안너큰")
                    .setContentText("졸음 방지 작동중")
                    .setOngoing(true);
        }
        else{
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.bee)
                    .setContentTitle("안너큰")
                    .setContentText("졸음 방지 작동중")
                    .setOngoing(true);
        }

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(3452, mBuilder.build());//알림창 실행

        ////////////////////////////////////////////////////////////////////////////

        createCameraSource();//카메라 소스를 생성하며 아이트레킹 시작
        my_timer.schedule(my_task, 1, 2000); // 타이머 실행

        return super.

                onStartCommand(intent, flags, startId);


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
                count = 0;//눈이 감지된다면 시간초 카운트를 0으로 한다
            } else if (face.getIsLeftEyeOpenProbability() < THRESHOLD || face.getIsRightEyeOpenProbability() < THRESHOLD) {
                Log.e("eye close", "eye close");

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




