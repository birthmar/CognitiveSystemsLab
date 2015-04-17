package de.tucottbus.kt.csl.midi.io;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiReceiver implements Receiver {

  private String name;
  private MidiParser parser = new MidiParser();

  public MidiReceiver(String name) {
    this.setName(name);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void send(MidiMessage msg, long timeStamp) {
    parser.parsceHexToIntString(msg, timeStamp);
  }

  public void close() {
  }

}
