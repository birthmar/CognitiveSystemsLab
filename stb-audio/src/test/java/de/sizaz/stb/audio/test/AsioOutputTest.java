package de.sizaz.stb.audio.test;

import de.sizaz.stb.audio.device.AAsioAudioDevice;
import de.sizaz.stb.audio.device.AsioOutputDevice;

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
