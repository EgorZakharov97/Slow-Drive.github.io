package com.Logic.Interface;

import android.content.Context;
import android.media.AudioManager;

// Audio files interface
// Since this audio is called for one specific sound effect,
// The class can be made static and called whenever necessary.
// Audio cannot be stopped. As it is a response to the driver over-speeding
public interface IAudioMng {
    // Play audio
    public static void play() {
        System.out.println("An error has occurred when playing audio. You should not see this message.");
    }

    // Setup / initialize the audio
    public static void setsetupAudio(final Context audContext, final AudioManager service)
    {
        System.out.println ("An error has occurred setting up audio. Could not initialize Context and Manager services.");
    }
}
