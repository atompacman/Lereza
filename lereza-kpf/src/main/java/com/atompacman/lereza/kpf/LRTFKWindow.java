package com.atompacman.lereza.kpf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atompacman.lereza.core.midi.device.MIDIDeviceException;
import com.atompacman.lereza.core.midi.device.MIDIDeviceInfo;
import com.atompacman.lereza.core.midi.device.MIDIDeviceManager;
import com.atompacman.lereza.core.midi.device.MIDIDeviceType;
import com.atompacman.lereza.core.midi.realtime.PlayingTonesListener;
import com.atompacman.lereza.core.midi.realtime.RealTimeMIDIProcessor;
import com.atompacman.lereza.core.solfege.Tone;
import com.atompacman.lereza.pluggin.builtin.key.RealTimeKeyChangeDetector;
import com.atompacman.toolkat.IO;
import com.atompacman.toolkat.gui.CenteredJFrame;
import com.atompacman.toolkat.misc.Log;

@SuppressWarnings("serial")
public class LRTFKWindow extends CenteredJFrame {

    //====================================== CONSTANTS ===========================================\\

    // Labels
    private static final String WIN_TITLE = "Lereza Real-Time Key Finder";

    // Dimensions
    private static final Dimension WIN_DIM                 = new Dimension(500, 700);
    private static final int       BORDER_WIDTH            = 10;
    private static final int       DEVICE_SEL_LABEL_SIZE   = 12;
    private static final int       DEVICE_COMBO_BOX_HEIGHT = 40;
    private static final String    CONNEC_STATUS_MSG       = "Connection status: ";

    // Key path finder config
    public  static final String KCW_FILE                      = "default.kcw";
    private static final int    KEY_DETECTION_INTERVAL_MILLIS = 500; 
    private static final int    MILLIS_PER_TU                 = 20;

    // Key path finder instance
    private static RealTimeKeyChangeDetector KPF;
    static {
        try {
            int tuInterval = KEY_DETECTION_INTERVAL_MILLIS / MILLIS_PER_TU;
            KPF = new RealTimeKeyChangeDetector(IO.getResource(KCW_FILE), 64, 0.5, tuInterval);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    //===================================== INNER TYPES ==========================================\\

    private enum DeviceConnection {

        CONTROLLER_TO_PROCESSOR         ("MIDI controller",         MIDIDeviceType.MIDI_IN), 
        CONTROLLER_TO_PLAYBACK          ("MIDI playback output",    MIDIDeviceType.MIDI_OUT), 
        PROCESSOR_TO_PROCESSED_PLAYBACK ("MIDI processed output",   MIDIDeviceType.MIDI_OUT);


        //===================================== FIELDS ===========================================\\

        private final String uiName;
        private final MIDIDeviceType assocType;



        //===================================== METHODS ==========================================\\

        //-------------------------------- PRIVATE CONSTRUCTOR -----------------------------------\\

        private DeviceConnection(String uiName, MIDIDeviceType assocType) {
            this.uiName = uiName;
            this.assocType = assocType;
        }
    }



    //======================================= FIELDS =============================================\\

    // MIDI device manager
    private MIDIDeviceManager deviceManager;
    private MIDIDeviceInfo    controller;
    private JPanel            tonesLabel;



    //======================================= METHODS ============================================\\

    //---------------------------------------- MAIN ----------------------------------------------\\

    public static void main(String args[]) throws MIDIDeviceException {
        new LRTFKWindow();
    }    


    //---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

    public LRTFKWindow() throws MIDIDeviceException {
        super(WIN_DIM);
        
        // Set basic properties
        setTitle(WIN_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Get device manager
        deviceManager = MIDIDeviceManager.getInstance();

        // Create main panel
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        getContentPane().add(Box.createRigidArea(new Dimension(BORDER_WIDTH, 0)));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(0, BORDER_WIDTH)));

        // Create playing tones label
        tonesLabel = createLabelPanel("", DEVICE_SEL_LABEL_SIZE, JLabel.CENTER);
        mainPanel.add(tonesLabel);

        // Create device selection combo boxes
        for (DeviceConnection connec : DeviceConnection.values()) {
            mainPanel.add(createDeviceSelecBox(connec));
        }

        // Add main panel
        mainPanel.add(Box.createVerticalGlue());
        getContentPane().add(mainPanel);
        getContentPane().add(Box.createRigidArea(new Dimension(BORDER_WIDTH, 0)));

        // Show window
        setVisible(true);
    }

    private JPanel createDeviceSelecBox(DeviceConnection connec) 
            throws MIDIDeviceException {

        // Create box panel
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        // Add device selection label
        boxPanel.add(createLabelPanel(connec.uiName, DEVICE_SEL_LABEL_SIZE, JLabel.LEFT));

        // Add separation
        boxPanel.add(Box.createRigidArea(new Dimension(0, (int) (BORDER_WIDTH * 0.2))));

        // Create box
        List<MIDIDeviceInfo> deviceList = deviceManager.getDevicesOfType(connec.assocType);
        JComboBox<Object> box = new JComboBox<>(deviceList.toArray());
        box.setPrototypeDisplayValue("");
        box.setSelectedIndex(-1);
        box.setMaximumSize(new Dimension(2 * WIN_DIM.width, DEVICE_COMBO_BOX_HEIGHT));

        // Create connection status label
        JPanel statusLabel = createLabelPanel(CONNEC_STATUS_MSG + "Waiting "
                + "for selection", DEVICE_SEL_LABEL_SIZE, JLabel.CENTER);

        // Connect devices when an item is selected
        box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MIDIDeviceInfo selectedDevice = (MIDIDeviceInfo) box.getSelectedItem();

                switch (connec) {
                case CONTROLLER_TO_PROCESSOR:
                    try {
                        RealTimeMIDIProcessor rtmp = 
                                new RealTimeMIDIProcessor(KEY_DETECTION_INTERVAL_MILLIS) {
                            public void process(List<MidiEvent> events) {
                                KPF.detect(events); 
                            }
                        };
                        rtmp.start();
                        deviceManager.connectDeviceToReceiver(selectedDevice, 
                                rtmp, "Real-time key finder");
                        deviceManager.connectDeviceToReceiver(selectedDevice, 
                                new PlayingTonesListener() {
                            public void update(Set<Tone> tones) {
                                StringBuilder sb = new StringBuilder();
                                for (Tone tone : tones) {
                                    sb.append(tone.toString()).append(' ');
                                }
                                JLabel label = (JLabel) tonesLabel.getComponent(0);
                                label.setText(sb.toString());
                            }
                        }, "Playing tones listener");
                    } catch (MIDIDeviceException e1) {
                        setConnectionStatusError(e1.getMessage(), statusLabel);
                        return;
                    }
                    controller = selectedDevice;
                    break;
                case CONTROLLER_TO_PLAYBACK:
                    if (controller == null) {
                        setConnectionStatusError("No controller selected", statusLabel);
                        return;
                    }
                    try {
                        deviceManager.connectDevices(controller, selectedDevice);
                    } catch (MIDIDeviceException e1) {
                        setConnectionStatusError(e1.getMessage(), statusLabel);
                        return;
                    }
                    break;
                case PROCESSOR_TO_PROCESSED_PLAYBACK:
                    setConnectionStatusError("Unimplemented", statusLabel);
                    return;
                }
                setConnectionStatusOK(statusLabel);
            }
        });
        boxPanel.add(box);

        // Add separation
        boxPanel.add(Box.createRigidArea(new Dimension(0, BORDER_WIDTH)));

        // Add labels
        boxPanel.add(statusLabel);
        boxPanel.add(tonesLabel);

        // Add separation
        boxPanel.add(Box.createRigidArea(new Dimension(0, BORDER_WIDTH)));

        return boxPanel;
    }

    private void setConnectionStatusError(String msg, JPanel statusLabel) {
        JLabel status = ((JLabel) statusLabel.getComponent(0));
        status.setText(msg);
        status.setForeground(Color.RED);
        Log.error(msg);
    }

    private void setConnectionStatusOK(JPanel statusLabel) {
        JLabel status = ((JLabel) statusLabel.getComponent(0));
        status.setText(CONNEC_STATUS_MSG + "Ok");
        status.setForeground(Color.GREEN);
    }
}
