package de.tucottbus.kt.csl.audio.device;

import com.github.rjeschke.jpa.JPA;
import com.github.rjeschke.jpa.PaBuffer;
import com.github.rjeschke.jpa.PaCallback;
import com.github.rjeschke.jpa.PaDeviceInfo;
import com.github.rjeschke.jpa.PaHostApiInfo;
import com.github.rjeschke.jpa.PaHostApiType;
import com.github.rjeschke.jpa.PaStreamParameters;

import de.tucottbus.kl.csl.Logg;

public class AsioOutputDevice extends AAsioAudioDevice {
  private final static String CLASSKEY = "ASIO Output";

  /**
   * The number of audio input channels.
   */
  private static final int CHANNELS = 2;

  private static int device = 0;

  /**
   * Device info of the associated ASIO device ID.
   */
  private static PaDeviceInfo di = null;

  private static AAsioAudioDevice instance;

  public AsioOutputDevice() {
    super();
  }

  /**
   * 
   * @return
   */
  public static AAsioAudioDevice getInstance() {
    if (instance == null) {
      instance = new AsioOutputDevice();
    }
    return instance;
  }

  @Override
  protected boolean findAudioDevice() {
    for (int d = 0; d < JPA.getDeviceCount(); d++) {
      di = JPA.getDeviceInfo(d);
      PaHostApiInfo hi = JPA.getHostApiInfo(di.getHostApi());

      if (hi.getType() != PaHostApiType.paASIO)
        continue;

      String diName = di.getName();
      Logg.msg(CLASSKEY, "Device #" + d + ": " + diName + "(" + hi.getName()
          + "), Max channel: " + di.getMaxInputChannels());

      if (diName.equals(CLASSKEY)) {
        device = d;
        return true;
      }
    }
    Logg.err(CLASSKEY, "ERROR: There is no ASIO device named"
        + " \"ASIO Hammerfall DSP\".");
    return false;
  }

  /**
   * Setting all input parameters for
   * 
   * @param inParameters
   *          Create of input parameters.
   */
  protected PaStreamParameters createAudioParam() {
    return new PaStreamParameters(device, CHANNELS, sample_param,
        di.getDefaultHighInputLatency());
  }
  
  @Override
  public void audioCallback() {
    // TODO Auto-generated method stub
    
  }

  /**
   * Callback method to get the audio data
   */
  public void audioCallback(PaBuffer outputBuffer) {
    PaCallback callback = new PaCallback() {
      @Override
      public void paCallback(PaBuffer inputBuffer, PaBuffer outputBuffer,
          int arg2) {
          // TODO: mach hier was
          //Audio.input(inputBuffer);
      }
    };
    JPA.setCallback(callback);
  }

  public static int getChannel() {
    return CHANNELS;
  }

  @Override
  public String getName() {
    return CLASSKEY;
  }


}
