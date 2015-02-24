package de.tucottbus.kt.csl.midi.io;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import de.tucottbus.kl.csl.Logg;

public class MidiTransmitter {
  private String classkey = getClass().getName();
  
  private Receiver receiver;
  private MidiParser p = new MidiParser();
  
  private int tick = 1;

  public MidiTransmitter(Receiver receiver)
  {
    this.receiver=receiver;  
  }

  public void sendTestData()
  {
        int min = 71;
        int max = 77;
        
        //int maxR = 78;
        //int minR = 84;
        int channel = 1;
        while(true){
          for(int i=min;i<=max;i++){
            sendSingleMidiMsg(ShortMessage.CONTROL_CHANGE, channel, i, 127);
            sendSingleMidiMsg(ShortMessage.CONTROL_CHANGE, channel, i, 0);
          }
          for(int i=max;i>=min;i--){
            sendSingleMidiMsg(ShortMessage.CONTROL_CHANGE, channel, i, 127);
            sendSingleMidiMsg(ShortMessage.CONTROL_CHANGE, channel, i, 0);
          }
        }
  }
  
  public void sendSingleMidiMsg(int command, int channel, int note, int value) {
    try {
      ShortMessage shortMessage = new ShortMessage();
      printMsg(command, channel, note, value);
      channel--;
      shortMessage.setMessage(command, channel, note, value);
      receiver.send(shortMessage, tick++);
      Thread.sleep(100);
    } catch (InvalidMidiDataException e) {
      Logg.err(classkey, "Invalid midi data: "+e.getCause());
    } catch (InterruptedException e) {
      Logg.err(classkey, "Thread interrupt expection: "+e.getCause());
    }
  }
  
  private void printMsg(int command, int channel, int note, int value) {
    Logg.msg(classkey,"Midi sending:  Channel=" + channel + " Command="
        + p.getMidiCommandString(command) + " Note="
        + p.getKeyName(note) + " Key=" + note + " value=" + value);
  }

}
