package de.tucottbus.kt.csl.audio.device;

import javax.sound.sampled.AudioFormat;

import de.tucottbus.kt.csl.core.hardware.Hardware;

public abstract class IAudioDevice extends Hardware{
  /**
   * Default Sample Rate
   */
  private static final int SAMPLERATE = 44100;
  
  /**
   * All variables of a Java audio stream.
   * 
   * @return Returns the audio format for an audio stream.
   */
  public static AudioFormat getAudioFormat() {
    int sampleSizeInBits = 16; // 8,16
    int channels = 1; // 1,2
    boolean signed = true;
    boolean bigEndian = false;
    return new AudioFormat(SAMPLERATE, sampleSizeInBits, channels, signed,
        bigEndian);
  }
  
  /**
   * 
   * @return
   */
  public static int getSampleRate(){
    return SAMPLERATE;
  }
  
  /**
   * Abstract method to find a common audio device
   */
  protected abstract boolean findAudioDevice();  
}
