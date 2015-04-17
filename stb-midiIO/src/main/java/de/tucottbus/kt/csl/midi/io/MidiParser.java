package de.tucottbus.kt.csl.midi.io;

import javax.sound.midi.*;

import de.tucottbus.kl.csl.Logg;

public class MidiParser {
  private String classkey = getClass().getName();

  public final static String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#",
      "G", "G#", "A", "A#", "B" };
  public final static char[] HEY_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7',
      '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

  public String parsceHexToIntString(MidiMessage msg, long timeStamp) {
    StringBuilder msgString = new StringBuilder();
    msgString.append("Midi received: ");

    if (msg instanceof ShortMessage) {
      ShortMessage sm = (ShortMessage) msg;

      int ch = sm.getChannel() + 1;
      msgString.append("Channel=" + ch + " ");
      int key = sm.getData1();
      int octave = (key / 12) - 1;
      String noteName = getKeyName(key);
      int value = sm.getData2();

      int command = sm.getCommand();
      String midiCommand = getMidiCommandString(command);

      if (command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF
          || command == ShortMessage.CONTROL_CHANGE) {
        msgString.append("Command=" + midiCommand + " Note=" + noteName
            + octave + " key=" + key + " value=" + value);
      } else {
        msgString.append("Command=" + midiCommand + " value=" + value);
      }

    } else {
      msgString.append("Other message: " + msg.getClass());
    }

    Logg.msg(classkey, "" + msgString.toString());

    return msgString.toString();
  }

  public String getMidiCommandString(int command) {
    String midiCommand = null;

    switch (command) {
    case ShortMessage.NOTE_ON:
      midiCommand = "Note on";
      break;
    case ShortMessage.NOTE_OFF:
      midiCommand = "Note off";
      break;
    case ShortMessage.CONTROL_CHANGE:
      midiCommand = "Control change";
      break;
    case ShortMessage.PITCH_BEND:
      midiCommand = "Pitch bend";
      break;
    case ShortMessage.MIDI_TIME_CODE:
      midiCommand = "Midi time code";
      break;
    default:
      midiCommand = Integer.toString(command);
      break;
    }
    return midiCommand;
  }

  public String getKeyName(int nKeyNumber) {
    if (nKeyNumber > 127) {
      return "illegal value";
    } else {
      int nNote = nKeyNumber % 12;
      int nOctave = nKeyNumber / 12;
      return NOTE_NAMES[nNote] + (nOctave - 1);
    }
  }

}
