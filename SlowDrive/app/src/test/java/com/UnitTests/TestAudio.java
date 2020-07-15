package com.UnitTests;

import com.Logic.Audio;
import com.R;

import androidx.appcompat.app.AppCompatActivity; // For testing
import android.media.AudioManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// NOTE cannot run this class without assertions enabled
// All tests should run with them enabled! [Even if not required]

// It will be more practical to test if audio plays than to build a simulation so that we
// can determine if the audio is actually playing on a valid Context / AudioManager
public class TestAudio {

    // Run the test
    // Returns the test being runned
    public void run ()
    {
        blackBox();
    }

    // Perform a black box test on audio class
    public void blackBox ()
    {
       setupTest();
       playTest();
    }

    @Before
    public void setupTest ()
    {
        assertFalse("Test Failed: The fake sound was played", Audio.play(-1));

        Audio.setupAudio(null, null);
        assertFalse ("Test Failed: Setup audio with no context or manage", Audio.manager() != null);


        Audio.setupAudio(new fakeContext(), null);
        assertFalse ("Test Failed: Setup audio with no context or manage", Audio.manager() != null);

        // This should in theory still setup
        Audio.setupAudio(new fakeContext(), (AudioManager) (new fakeContext()).getSystemService(""));
        assertTrue ("Test Failed: Setup audio with no context or manage", Audio.manager() == null);
    }

    @Test
    public void playTest ()
    {

        // From prior test, this should work
        Audio.setupAudio(new fakeContext(), (AudioManager) (new fakeContext()).getSystemService(""));
        assertTrue ("Test Failed: Setup audio with no context or manage", Audio.manager() == null);

        // But, now an error should be caught and it should not work
        assertFalse ("Test Failed: exception was not thrown on fake audio", Audio.play());
        assertFalse ("Test Failed: exception was not thrown on fake audio", Audio.play(-1));

        // Play real sound, should still throw failed to run exception
        assertFalse ("Test Failed: exception was not thrown on fake audio", Audio.play(R.raw.speeding));
    }

    private class fakeContext extends AppCompatActivity {
        private fakeContext (){}
    }
}