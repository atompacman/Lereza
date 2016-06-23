package com.atompacman.lereza.core.midi.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.jfugue.midi.MidiDictionary;
import org.jfugue.player.Player;

import com.atompacman.lereza.core.midi.MIDIInstrument;
import com.atompacman.lereza.core.midi.MIDIInstrument.Family;

public class StaccatoInterpreter {

    //====================================== CONSTANTS ===========================================\\

    private static final Set<String> EXIT_KEYWORDS = new HashSet<>(Arrays.asList("exit, quit"));
    private static final String INSTRUMENT_SELECTION_KEYWORD = "I";



    //==================================== STATIC FIELDS =========================================\\

    private static Player  staccatoPlayer;
    private static Scanner keyboard;
    private static String  instrument;



    //======================================= METHODS ============================================\\

    //---------------------------------------- MAIN ----------------------------------------------\\

    public static void main(String[] args) {
        // Init
        staccatoPlayer = new Player();
        keyboard       = new Scanner(System.in);
        instrument     = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get((byte)0);

        // Main loop
        System.out.println("Staccato interpreter");
        
        while (true) {
            System.out.print("> ");
            String input = keyboard.nextLine();
            
            if (EXIT_KEYWORDS.contains(input)) {
                break;
            }
            
            try {
                if (input.equalsIgnoreCase(INSTRUMENT_SELECTION_KEYWORD)) {
                    chooseInstrument();
                } else {
                    if (!input.startsWith(INSTRUMENT_SELECTION_KEYWORD)) {
                        input = "I[" + instrument + "] " + input;
                    }
                    staccatoPlayer.play(input);
                }
            } catch (Exception e) {
                System.out.println("ERROR: Invalid input");
            }
        }
        keyboard.close();
    }

    private static void chooseInstrument() {
        printListOfElements(new ArrayList<>(Arrays.asList(Family.values())));
        System.out.print("Choose a category: ");
        Family family = Family.values()[keyboard.nextInt() - 1];

        printListOfElements(new ArrayList<>(MIDIInstrument.listInstruments(family)));
        System.out.print("Choose an instrument: ");
        byte id = (byte) (family.ordinal() * 8 + keyboard.nextInt() - 1);
        keyboard = new Scanner(System.in);
        instrument = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get(id);
    }

    private static <T extends Enum<?>> void printListOfElements(List<T> list) {
        printLine();
        int h = (int) (list.size() * 0.5);
        for (int i = 0; i < h; ++i) {
            System.out.println(String.format("(%d) %-30s (%2d) %s ",
                    (i+1), list.get(i), (i+h+1), list.get(i+h)));
        }
        printLine();
    }

    private static void printLine() {
        for (int i = 0; i < 70; ++i) {
            System.out.print('-');
        }
        System.out.println();
    }
}