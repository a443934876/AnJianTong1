package org.chinasafety.liu.anjiantong.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.chinasafety.liu.anjiantong.R;
import org.chinasafety.liu.anjiantong.utils.RxCountDown;
import org.chinasafety.liu.anjiantong.view.BaseActivity;
import org.chinasafety.liu.anjiantong.view.widget.ArcProgress;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 录音
 *
 * @author Administrator
 */
public class AudioRecordActivity extends BaseActivity {
    public static final String TAG_RECORD = "record";
    public static final String TAG_PLAY = "play";
    public static final String TAG_PAUSE = "pause";
    public static final String TAG_FINISH = "finish";
    private Button mAudioStartBtn;
    private Button mAudioStopBtn;
    private MediaRecorder mMediaRecorder;// MediaRecorder对象
    private String audioPath;
    private ArcProgress mArcProgress;
    private MediaPlayer mMediaPlayer;
    private Disposable mSubscription;
    private int mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediarecorderdemo1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAudioStartBtn = (Button) findViewById(R.id.mediarecorder1_AudioStartBtn);
        mAudioStopBtn = (Button) findViewById(R.id.mediarecorder1_AudioStopBtn);
        mArcProgress = (ArcProgress) findViewById(R.id.audio_progress_time);
        audioPath = getIntent().getStringExtra("mAudioPath");

		/* 按钮状态 */
        mAudioStartBtn.setEnabled(true);
        mAudioStopBtn.setEnabled(false);
        addLinster();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 添加监听
     */
    private void addLinster() {
        /* 开始按钮事件监听 */
        mAudioStartBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    startRecord();
                } else if (v.getTag().toString().equals(TAG_RECORD)) {
                    play();
                } else if (v.getTag().toString().equals(TAG_PLAY)) {
                    pause();
                } else if (v.getTag().toString().equals(TAG_PAUSE)) {
                    play();
                }

            }

        });

		/* 停止按钮事件监听 */
        mAudioStopBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    stop();
                } else if (v.getTag().equals(TAG_FINISH)) {
                    Intent intent = new Intent();
                    intent.putExtra("result", audioPath);
                    AudioRecordActivity.this.setResult(Activity.RESULT_OK, intent);
                    finish();
                }

            }

        });
    }

    private void startRecord() {
        mMediaRecorder = new MediaRecorder();
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setOutputFile(audioPath);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "没有录音权限,请开启权限！", Toast.LENGTH_SHORT).show();
            return;
        }
        /* 按钮状态 */
        mAudioStartBtn.setText("播放");
        mAudioStartBtn.setTag(TAG_RECORD);
        mAudioStartBtn.setEnabled(false);
        mAudioStopBtn.setEnabled(true);
        startTime(mArcProgress.getMax(), false);
    }

    private void stop() {
        mAudioStopBtn.setText("录音完成");
        mAudioStopBtn.setTag(TAG_FINISH);
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }


        mAudioStartBtn.setEnabled(true);
        mAudioStartBtn.setText("播放");
        stopTime();

    }

    private void play() {
        mAudioStartBtn.setTag(TAG_PLAY);
        mAudioStartBtn.setText("停止");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer pMediaPlayer) {
                mMediaPlayer.release();
                mAudioStartBtn.setTag(TAG_PAUSE);
                mAudioStartBtn.setText("播放");
                stopTime();
            }
        });
        try {
            mMediaPlayer.setDataSource(audioPath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mArcProgress.setMax(mTime);
            startTime(mTime, true);
        } catch (Exception pE) {
            pE.printStackTrace();
        }
    }

    private void pause() {
        mAudioStartBtn.setTag(TAG_PAUSE);
        mAudioStartBtn.setText("播放");
        mMediaPlayer.stop();
        mMediaPlayer.release();
        stopTime();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void stopTime() {
        if (mSubscription != null) {
            mSubscription.dispose();
        }
        if (mTime == 0) {
            mTime = mArcProgress.getMax() - mArcProgress.getProgress();
        }
    }

    private void startTime(int max, boolean isPlay) {
        RxCountDown.countdown(max, isPlay)
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        stop();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mSubscription = d;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        mArcProgress.setProgress(integer);
                    }
                });
    }
}
