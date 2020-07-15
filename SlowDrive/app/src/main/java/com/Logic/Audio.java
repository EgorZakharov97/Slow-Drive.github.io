package com.Logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.AudioManager;
import android.util.Log;

import com.CustomExceptions.AudioErrorException;

import com.Logic.Interface.IAudioMng;
import com.R;
import com.UI.SettingsActivity;

import java.io.IOException;

// STATIC CLASS
// This class plays an audio sound effect to warn the driver
public class Audio implements IAudioMng {

    private static final String TAG = "AUDIO_MANAGER";
    private static MediaPlayer player;
    private static Context context; // REQUIRED for changing audio files
    private static AudioManager manager;
    private static SharedPreferences prefs;
    private static int curFile = -1;




    // Setup audio to the current device
    // Will only setup once
    public static void setupAudio (final Context audContext, final AudioManager service) {
        if (audContext == null || service == null) {
            System.out.println("Service and context could not be identified.");
            return;
        }
        manager = service;

        // Initialize player on first call
        if (player == null) {
            //AppCompatActivity
            player = MediaPlayer.create(audContext, R.raw.speeding);
            curFile = R.raw.speeding;

            if (context == null) {
                context = audContext; // Store encase of audio change
            }
            prefs = context.getSharedPreferences(SettingsActivity.SETTINGS_PREFS, Context.MODE_PRIVATE);

            try {
                prepareAudio();
            } catch (AudioErrorException e) {
                System.err.println(e);
            }
        }
    }

    public static void setVolume(int volume){
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        Log.d(TAG, "Volume was set to " + volume);
    }

    // By default, we will just play the speeding sound effect
    // Our application does not have many sounds, so we can assume this is default
    // This is easily changed as play (int) already manages new sound effects
    public static boolean play ()
    {
        // set audio volume
        try {
            int volume = prefs.getInt(SettingsActivity.VOLUME, 80);
            Audio.setVolume(volume);
        }catch (Exception e){
            return false;
        }
        return play (R.raw.speeding);
    }

    // Play the given sound effect
    // The sound effect will be one to alert the user to no longer speed
    public static boolean play (int soundToPlay)
    {
        try {
            // set the audio volume
            int volume = prefs.getInt(SettingsActivity.VOLUME, 80);
            Audio.setVolume(volume);
            // Need reference first
            // This is necessary as it can occur if a user is already speeding before opening app
            if (player == null || manager == null)
                return false;

            // Setup to play a different sound effect
            if (soundToPlay != curFile && context != null) {
                // Stop audio to play a different one
                if (player.isPlaying())
                    player.stop();

                curFile = soundToPlay;
                player.reset();
                //player.set;
                player = MediaPlayer.create(context, soundToPlay);

                try {
                    prepareAudio();
                } catch (AudioErrorException e) {
                    System.err.println(e);
                    return false;
                }
            }

            // Do not constantly re-play the same sounds
            if (player.isPlaying())
                return false;

            // Grab the current volume
            int curVol = manager.getStreamVolume(manager.STREAM_MUSIC);

            // Set volume to max
            setMaxUserVolume();

            player.start(); // Begin audio


            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                private int curVol = -1;

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (curVol == -1) {
                        System.out.println("Invalid volume error occurred.");
                        System.out.println("Will not change the volume.");
                    } else {
                        // Reset volume back to normal once the audio is complete
                        try {
                            manager.setStreamVolume
                                    // Ringer mode silent flag prevents changing volume UI being displayed
                                            (manager.STREAM_MUSIC, curVol, AudioManager.RINGER_MODE_SILENT);
                        } catch (SecurityException e) {
                            System.out.println("Volume changed on Do No Disturb, cannot change the volume");
                            e.printStackTrace();
                        }
                    }
                }

                // Modify volume for anonymous class [java hack]
                private MediaPlayer.OnCompletionListener init(int volume) {
                    this.curVol = volume;
                    return this;
                }
            }.init(curVol));
        }catch (Exception e){
            return false;
        }
        return true; // SUCCESS!
    }

    // Prepare the audio files
    // There are more than one exceptions that can occur here
    private static void prepareAudio () throws AudioErrorException {
        try {
            player.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            throw new AudioErrorException("An error occurred playing Audio", e);
        }
    }

    private static void setMaxUserVolume ()
    {
        try {
            manager.setStreamVolume
                    // Ringer mode silent flag prevents changing volume UI being displayed
                            (manager.STREAM_SYSTEM, manager.getStreamMaxVolume(manager.STREAM_SYSTEM), AudioManager.RINGER_MODE_SILENT);
        } catch (SecurityException e)
        {
            System.out.println ("Volume changed on Do No Disturb, cannot change the volume");
            e.printStackTrace();
        }
    }

    // Encase you wish to modifier the audio yourself
    public static AudioManager manager ()
    {
        return manager;
    }
}