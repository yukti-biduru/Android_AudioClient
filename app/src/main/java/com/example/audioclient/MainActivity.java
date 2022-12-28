package com.example.audioclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import com.example.clipserver.MediaServiceInterface;

public class MainActivity extends AppCompatActivity {

    private MediaServiceInterface mediaServiceInterface;
    boolean isServiceBound;
    boolean isClipClicked  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button start = findViewById(R.id.start);
        Button pause = findViewById(R.id.pause);
        Button resume = findViewById(R.id.resume);
        Button stop = findViewById(R.id.stop);

        Button clip1 = findViewById(R.id.clip1);
        Button clip2 = findViewById(R.id.clip2);
        Button clip3 = findViewById(R.id.clip3);
        Button clip4 = findViewById(R.id.clip4);
        Button clip5 = findViewById(R.id.clip5);


            if (!isClipClicked) {
                start.setEnabled(false);
                pause.setEnabled(false);
                resume.setEnabled(false);
                stop.setEnabled(false);
            }

        start.setOnClickListener(v -> {
            if(isServiceBound)
            {
                startMedia(mediaServiceInterface);
                pause.setEnabled(true);
                resume.setEnabled(false);
                stop.setEnabled(true);
            }
            else
            {
                Log.i("Logger", "Service not bound");
            }
        });

        pause.setOnClickListener(v -> {
            if(isServiceBound)
            {
                pauseMedia(mediaServiceInterface);
                start.setEnabled(false);
                resume.setEnabled(true);
                stop.setEnabled(false);
            }
            else
            {
                Log.i("Logger", "Service not bound");
            }
        });

        resume.setOnClickListener(v -> {
            if(isServiceBound)
            {
                resumeMedia(mediaServiceInterface);
                start.setEnabled(false);
                pause.setEnabled(true);
                stop.setEnabled(true);
            }
            else
            {
                Log.i("Logger", "Service not bound");
            }
        });

        stop.setOnClickListener(v -> {
            if(isServiceBound)
            {
                stopMedia(mediaServiceInterface);
                start.setEnabled(true);
                resume.setEnabled(false);
                pause.setEnabled(false);
                unbindService(this.serviceConnection);
            }
            else
            {
                Log.i("Logger", "Service not bound");
            }
        });

        clip1.setOnClickListener(v -> {
                setAudioClipNumber(1);
            isClipClicked = true;
        });
        clip2.setOnClickListener(v -> {
            setAudioClipNumber(2);
            isClipClicked = true;

        });
        clip3.setOnClickListener(v -> {
            setAudioClipNumber(3);
            isClipClicked = true;

        });
        clip4.setOnClickListener(v -> {
            setAudioClipNumber(4);
            isClipClicked = true;

        });
        clip5.setOnClickListener(v -> {
            setAudioClipNumber(5);
            isClipClicked = true;
        });

    }

    @Override
    protected void onStop() {

        super.onStop();

        if (isServiceBound) {
            unbindService(this.serviceConnection);
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaServiceInterface = MediaServiceInterface.Stub.asInterface(service);
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mediaServiceInterface = null;
            isServiceBound = false;
        }
    };

    private void startMedia (MediaServiceInterface mediaServiceInterface)
    {
        try {
            mediaServiceInterface.startMedia();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void resumeMedia (MediaServiceInterface mediaServiceInterface)
    {
        try {
            mediaServiceInterface.resumeMedia();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void pauseMedia (MediaServiceInterface mediaServiceInterface)
    {
        try {
            mediaServiceInterface.pauseMedia();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopMedia (MediaServiceInterface mediaServiceInterface)
    {
        try {
            mediaServiceInterface.stopMedia();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setAudioClipNumber (int clipNumber)
    {
        try {
            bindMediaService();
            mediaServiceInterface.setAudioClipNumber(clipNumber);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindMediaService()
    {
        if (!isServiceBound) {

            Intent i = new Intent(MediaServiceInterface.class.getName());

//            PackageManager info = getPackageManager();
            i.setComponent(new ComponentName("com.example.clipserver", "com.example.clipserver.ClipService"));

            // unable to solve binding here. Service isnt binding and thus object for connection is null and getting NPE.
            isServiceBound = bindService(i, this.serviceConnection, Context.BIND_EXTERNAL_SERVICE);
        }
    }

}