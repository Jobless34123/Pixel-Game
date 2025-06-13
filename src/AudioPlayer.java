import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class AudioPlayer {
    private Clip clip;

    public AudioPlayer(String musicPath) {
        System.out.println("Trying to load: " + musicPath);
        System.out.println("Resource URL: " + getClass().getResource(musicPath));
        try {
            // Get the audio stream from the resource folder.
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource(musicPath));

            // Obtain a Clip to play the sound
            clip = AudioSystem.getClip();
            clip.open(audioStream);   // Open audio clip and load samples from the audio input stream.
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Call this method to start the music loop.
    public void playLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Optionally, stop the music.
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}
