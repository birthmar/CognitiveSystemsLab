package de.sizaz.stb.midi.device;

import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

import de.sizaz.stb.Logg;
import de.sizaz.stb.core.hardware.Hardware;
import de.sizaz.stb.midi.io.MidiReceiver;
import de.sizaz.stb.midi.io.MidiTransmitter;

public class MidiIODevice extends Hardware {
  private String classkey = getClass().getName();

  public final static String DEFAULT_DEVICE = "TouchOSC Bridge";

  private MidiDevice inDevice = null;
  private MidiDevice outDevice = null;

  private ArrayList<MidiDevice> inDeviceList = new ArrayList<MidiDevice>();
  private ArrayList<MidiDevice> outDeviceList = new ArrayList<MidiDevice>();

  private MidiReceiver receiver;
  private MidiTransmitter transmitter;

  private String midiInDeviceName = null;
  private String midiOutDeviceName = null;

  private boolean defaultDeviceFound = false;
  private boolean activated = false;
  
  private static MidiIODevice instance = null;

  private MidiIODevice() {
    initMidiDeviceLists();
    while(!defaultDeviceFound) {
      startDefaultMidiDevice();
      if(initMidiDeviceLists())
        break;
    }
    registerMidiDevice();
    activated=true;
  }

  public static MidiIODevice getInstance() {
    if (instance == null) {
      instance = new MidiIODevice();
    }
    return instance;
  }

  public boolean initMidiDeviceLists() {
    MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    MidiDevice device = null;

    for (int i = 0; i < infos.length; i++) {
      if (infos[i].getName().contains(DEFAULT_DEVICE)) {
        defaultDeviceFound = true;
      }
    }

    for (int i = 0; i < infos.length; i++) {
      try {
        device = MidiSystem.getMidiDevice(infos[i]);
      } catch (MidiUnavailableException e1) {
        Logg.warn(classkey, "Default device '" + DEFAULT_DEVICE
            + "' could not found. Error: " + e1.getMessage());
      }

      if (infos[i].getClass().toString().contains("MidiInDevice")) {
        midiInDeviceName = infos[i].getDescription();
        inDeviceList.add(device);
        inDevice = device;
      }

      if (infos[i].getClass().toString().contains("MidiOutDevice")) {
        midiOutDeviceName = infos[i].getDescription();
        outDeviceList.add(device);
        outDevice = device;
      }
    }

    if (!defaultDeviceFound) {
      Logg.warn(classkey, "Default device '" + DEFAULT_DEVICE
          + "' could not found.");
    }

    return defaultDeviceFound;
  }

  public void startDefaultMidiDevice() {
    try {
      if (System.getProperty("os.name").contains("Mac OS X")) {
        Process p = Runtime.getRuntime().exec(
            "open /Applications/TouchOSC/TouchOSCBridge.app");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          Logg.err(classkey, "Interrupted exception error: " + e.getMessage());
        }
        
        if (p.isAlive()) {
          Logg.msg(classkey, "Default device '" + DEFAULT_DEVICE
              + "' was started automatically.");
        }
      }
    } catch (IOException e) {
      Logg.err(classkey, "Programm '" + DEFAULT_DEVICE + "' not found. Error: "
          + e.getMessage());
    }
  }

  public void registerMidiDevice() {
    try {
      if (inDevice != null && outDevice != null) {

        if (!inDevice.isOpen())
          inDevice.open();
        Transmitter t = inDevice.getTransmitter();
        if (t == null)
          Logg.err(classkey, inDevice + ".getTransmitter() == null");

        if (!outDevice.isOpen())
          outDevice.open();
        receiver = new MidiReceiver(outDevice.getDeviceInfo().toString());

        if (t != null && receiver != null) {
          t.setReceiver(receiver);
          transmitter = new MidiTransmitter(MidiSystem.getReceiver());
        }
      }
    } catch (MidiUnavailableException e) {
      Logg.err(classkey,
          "The registstration of the midi device failed: " + e.getCause());
    }
  }

  public void initMidiInputDevice(int deviceNo) {
    if (inDevice != null)
      inDevice.close();
    inDevice = (MidiDevice) inDeviceList.get(deviceNo);
    registerMidiDevice();
  }

  public void initMidiOutputDevice(int deviceNo) {
    if (outDevice != null)
      outDevice.close();
    outDevice = (MidiDevice) outDeviceList.get(deviceNo);
    registerMidiDevice();
  }

  public String getMidiInDeviceName() {
    return midiInDeviceName;
  }

  public String getMidiOutDeviceName() {
    return midiOutDeviceName;
  }

  public MidiTransmitter getTransmitter() {
    return transmitter;
  }

  public boolean isDefaultDeviceAvailable() {
    return defaultDeviceFound;
  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isDisposed() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isConnected() {
    return activated;
  }

}
