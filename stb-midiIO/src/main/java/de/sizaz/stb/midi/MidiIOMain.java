package de.sizaz.stb.midi;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;

import de.sizaz.stb.Logg;
import de.sizaz.stb.midi.device.MidiIODevice;
import de.sizaz.stb.midi.gui.MidiGUI;

public class MidiIOMain {
  private String CLASSKEY = "Midi IO";
  
  private final static int PORT = 63000;
  private ServerSocket socket;
  
  public MidiIOMain(){
    checkRunningMidiIOMain();
    setupOSXMenuBar();
    new MidiGUI();
  }
  
  private void checkRunningMidiIOMain() {
    try {
      socket = new ServerSocket(PORT, 0, InetAddress.getByAddress(new byte[] {
          127, 0, 0, 1 }));
    } catch (BindException e) {
      Logg.err(CLASSKEY, "already running.");
      System.exit(1);
    } catch (IOException e) {
      Logg.err(CLASSKEY, "Unexpected error: "+e.getMessage());
      System.exit(2);
    }
    socket.getInetAddress();
  }
  
  private void setupOSXMenuBar(){
    if(System.getProperty("os.name").contains("Mac OS X")){
      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Midi IO");
    }
  }
  
  public static void main(String[] args) {
    new MidiIOMain();
  }

}
