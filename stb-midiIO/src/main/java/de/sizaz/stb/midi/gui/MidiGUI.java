package de.sizaz.stb.midi.gui;

import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.apple.eawt.Application;

import de.sizaz.stb.Logg;
import de.sizaz.stb.midi.io.MidiHandler;

public class MidiGUI {
  private final static String ICON = "/midiIO_icon.png";

  private URL url = null;
  private String classkey = getClass().getName();

  private JFrame frame;

  private Choice midiInChoice = new Choice();
  private Choice midiOutChoice = new Choice();

  private MidiHandler handler;

  public MidiGUI() {
    try {
      url = this.getClass().getResource(ICON);
    } catch (Exception e) {
      Logg.err(classkey, "Cannot find URL for programm icon: " + e.getMessage());
    }
    handler = new MidiHandler();

    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        initGUI();
      }
    });

    if (handler.isDefaultDeviceAvailable())
      handler.sendTestMessages();
  }

  private void initGUI() {
    frame = new JFrame("Small MIDI IO System");
    frame.setLayout(new FlowLayout());

    if (url != null) {
      if (System.getProperty("os.name").contains("Mac OS X")) {
        Image image = Toolkit.getDefaultToolkit().getImage(url);
        Application.getApplication().setDockIconImage(image);
      }

      ImageIcon img = new ImageIcon(url);
      frame.setIconImage(img.getImage());
    }

    MenuBar mb = new MenuBar();
    Menu menu = new Menu("File");
    MenuItem quit = new MenuItem("Quit", new MenuShortcut('Q'));
    quit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        e.getSource();
        System.exit(0);
      }
    });
    menu.add(quit);
    mb.add(menu);

    frame.setMenuBar(mb);
    frame.add(new Label("MIDI IN: "));
    frame.add(midiInChoice);
    frame.add(new Label("   MIDI OUT: "));
    frame.add(midiOutChoice);

    if (handler.isDefaultDeviceAvailable()) {
      midiInChoice.addItem(handler.getMidiInDeviceName());
      midiOutChoice.addItem(handler.getMidiOutDeviceName());
    }

    midiInChoice.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getItemSelectable() == midiInChoice) {
          handler.initMidiInputDevice(midiInChoice.getSelectedIndex());
        }
        handler.registerMidiDevice();
      }
    });

    midiOutChoice.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getItemSelectable() == midiOutChoice) {
          handler.initMidiOutputDevice(midiOutChoice.getSelectedIndex());
        }
        handler.registerMidiDevice();
      }
    });

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.pack();
    frame.setVisible(true);
  }

}