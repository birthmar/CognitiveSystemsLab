package de.tucottbus.kt.csl.audio.test;

import de.tucottbus.kt.csl.audio.device.AAsioAudioDevice;
import de.tucottbus.kt.csl.audio.device.AsioOutputDevice;

public class AsioOutputTest {

  AAsioAudioDevice aod = null;

  public AsioOutputTest() {
    aod = AsioOutputDevice.getInstance();
    aod.startStream();
  }
  
  public void sendAudio(){
    aod.audioCallback();
  }

  public static void main(String[] args) {
    AsioOutputTest test = new AsioOutputTest();
    test.sendAudio();

  }

}
