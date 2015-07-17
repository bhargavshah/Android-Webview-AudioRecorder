package com.pearson.audiorecorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.util.Base64;

import java.io.File;
import java.io.IOException;

import com.pearson.audiorecorder.IOUtils;

/**
 * Created by P7109048 on 16-07-2015.
 */
public class AudioRecorder {
    private MediaRecorder recorder;
    private String outputFile = null;
    private Context mContext;

    public AudioRecorder(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void initialize() {
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.m4a";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.getMaxAmplitude();
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC); // 3 = AAC
        recorder.setOutputFile(outputFile);
        recorder.setAudioSamplingRate(32000);
        recorder.setAudioChannels(1);
    }

    @JavascriptInterface
    public void record() {
        try {
            recorder.prepare();
            recorder.start();
        }

        catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void stop() {
        recorder.stop();
        recorder.release();
        recorder  = null;
    }

    @JavascriptInterface
    public void play() {
        MediaPlayer m = new MediaPlayer();

        try {
            m.setDataSource(outputFile);
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            m.prepare();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        m.start();
    }

    @JavascriptInterface
    public String getDataURL() {
        String dataURL = "";
        try {
            byte[] byteArr = IOUtils.readFile(outputFile);
            String b64 = Base64.encodeToString(byteArr, 0);
            dataURL = "data:audio/x-m4a;base64," + b64;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataURL;
    }
}
