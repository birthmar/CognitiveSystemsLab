package de.sizaz.stb.midi.io;

import de.sizaz.stb.midi.device.MidiIODevice;

public class MidiHandler {

  public void sendTestMessages() {
    MidiIODevice.getInstance().getTransmitter().sendTestData();
  }
  
  public String getMidiInDeviceName() {
    return MidiIODevice.getInstance().getMidiInDeviceName();
  }

  public String getMidiOutDeviceName() {
    return MidiIODevice.getInstance().getMidiOutDeviceName();
  }
  
  public void initMidiInputDevice(int deviceNo) {
    MidiIODevice.getInstance().initMidiInputDevice(deviceNo);
  }

  public void initMidiOutputDevice(int deviceNo) {
    MidiIODevice.getInstance().initMidiOutputDevice(deviceNo);
  }
  
  public boolean isDefaultDeviceAvailable(){
    return MidiIODevice.getInstance().isDefaultDeviceAvailable();
  }
  
  
}
