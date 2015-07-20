package com.pearson.audiorecorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Base64;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    public void initialize(String fileName) {
        outputFile = getFullPath(fileName);

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
    public String getBase64(String fileName) {
        String b64 = "";
        try {
            File file = new File(getFullPath(fileName));
            byte[] bytes = loadFile(file);
            b64 = Base64.encodeToString(bytes, 0);
//            dataURL = "data:audio/x-m4a;base64," + b64;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b64;
    }

    @JavascriptInterface
    public void deleteFile(String fileName) {
        String fullPath = getFullPath(fileName);
        File file = new File(fullPath);
        file.delete();
    }

    private byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
    }

    private String getFullPath(String fileName) {
        return "/data/data/" + mContext.getPackageName() + "/" + fileName;
    }
}
