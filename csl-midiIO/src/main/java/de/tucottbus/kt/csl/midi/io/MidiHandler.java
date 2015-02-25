package de.tucottbus.kt.csl.midi.io;

import javax.sound.midi.*;

import de.tucottbus.kl.csl.Logg;

import java.io.IOException;
import java.util.ArrayList;

public class MidiHandler {
  public final static String DEFAULT_DEVICE = "TouchOSC Bridge";

  private String classkey = getClass().getName();

  private MidiDevice inDevice = null;
  private MidiDevice outDevice = null;

  private ArrayList<MidiDevice> inDeviceList = new ArrayList<MidiDevice>();
  private ArrayList<MidiDevice> outDeviceList = new ArrayList<MidiDevice>();

  private MidiReceiver receiver;
  private MidiTransmitter transmitter;

  private String midiInDeviceName = null;
  private String midiOutDeviceName = null;

  private boolean defaultDeviceFound = false;

  public MidiHandler() {
    createMidiDeviceLists();
    registerMidiDevice();
  }

  private void createMidiDeviceLists() {
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
      try {
        if (System.getProperty("os.name").contains("Mac OS X")) {
          Process p = Runtime.getRuntime().exec(
              "open /Applications/TouchOSC/TouchOSCBridge.app");
          if (p.isAlive()) {
            Logg.msg(classkey, "Default device '" + DEFAULT_DEVICE
                + "' was started automatically. Please restart.");
          }
        }
      } catch (IOException e) {
        Logg.err(classkey, "Programm '" + DEFAULT_DEVICE
            + "' not found. Error: " + e.getMessage());
      }
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
  }

  public void initMidiOutputDevice(int deviceNo) {
    if (outDevice != null)
      outDevice.close();
    outDevice = (MidiDevice) outDeviceList.get(deviceNo);
  }

  public String getMidiInDeviceName() {
    return midiInDeviceName;
  }

  public String getMidiOutDeviceName() {
    return midiOutDeviceName;
  }

  public void sendTestMessages() {
    transmitter.sendTestData();
  }

  public boolean isDefaultDeviceAvailable() {
    return defaultDeviceFound;
  }

}
