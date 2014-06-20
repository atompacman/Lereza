package atompacman.leraza.midi.utilities;

public class HexToNote {

	public static String toString(int hexValue) {
		switch (hexValue) {
		case 0x7F : return "G9";
		case 0x7E : return "F#9";
		case 0x7D : return "F9";
		case 0x7C : return "E9";
		case 0x7B : return "Eb9";
		case 0x7A : return "D9";
		case 0x79 : return "C#9";
		case 0x78 : return "C9";
		case 0x77 : return "B8";
		case 0x76 : return "Bb8";
		case 0x75 : return "A8";
		case 0x74 : return "Ab8";
		case 0x73 : return "G8";
		case 0x72 : return "F#8";
		case 0x71 : return "F8";
		case 0x70 : return "E8";
		case 0x6F : return "Eb8";
		case 0x6E : return "D8";
		case 0x6D : return "C#8";
		case 0x6C : return "C8";
		case 0x6B : return "B7";
		case 0x6A : return "Bb7";
		case 0x69 : return "A7";
		case 0x68 : return "Ab7";
		case 0x67 : return "G7";
		case 0x66 : return "F#7";
		case 0x65 : return "F7";
		case 0x64 : return "E7";
		case 0x63 : return "Eb7";
		case 0x62 : return "D7";
		case 0x61 : return "C#7";
		case 0x60 : return "C7";
		case 0x5F : return "B6";
		case 0x5E : return "Bb6";
		case 0x5D : return "A6";
		case 0x5C : return "Ab6";
		case 0x5B : return "G6";
		case 0x5A : return "F#6";
		case 0x59 : return "F6";
		case 0x58 : return "E6";
		case 0x57 : return "Eb6";
		case 0x56 : return "D6";
		case 0x55 : return "C#6";
		case 0x54 : return "C6";
		case 0x53 : return "B5";
		case 0x52 : return "Bb5";
		case 0x51 : return "A5";
		case 0x50 : return "Ab5";
		case 0x4F : return "G5";
		case 0x4E : return "F#5";
		case 0x4D : return "F5";
		case 0x4C : return "E5";
		case 0x4B : return "Eb5";
		case 0x4A : return "D5";
		case 0x49 : return "C#5";
		case 0x48 : return "C5";
		case 0x47 : return "B4";
		case 0x46 : return "Bb4";
		case 0x45 : return "A4";
		case 0x44 : return "Ab4";
		case 0x43 : return "G4";
		case 0x42 : return "F#4";
		case 0x41 : return "F4";
		case 0x40 : return "E4";
		case 0x3F : return "Eb4";
		case 0x3E : return "D4";
		case 0x3D : return "C#4";
		case 0x3C : return "C4";
		case 0x3B : return "B3";
		case 0x3A : return "Bb3";
		case 0x39 : return "A3";
		case 0x38 : return "Ab3";
		case 0x37 : return "G3";
		case 0x36 : return "F#3";
		case 0x35 : return "F3";
		case 0x34 : return "E3";
		case 0x33 : return "Eb3";
		case 0x32 : return "D3";
		case 0x31 : return "C#3";
		case 0x30 : return "C3";
		case 0x2F : return "B2";
		case 0x2E : return "Bb2";
		case 0x2D : return "A2";
		case 0x2C : return "Ab2";
		case 0x2B : return "G2";
		case 0x2A : return "F#2";
		case 0x29 : return "F2";
		case 0x28 : return "E2";
		case 0x27 : return "Eb2";
		case 0x26 : return "D2";
		case 0x25 : return "C#2";
		case 0x24 : return "C2";
		case 0x23 : return "B1";
		case 0x22 : return "Bb1";
		case 0x21 : return "A1";
		case 0x20 : return "Ab1";
		case 0x1F : return "G1";
		case 0x1E : return "F#1";
		case 0x1D : return "F1";
		case 0x1C : return "E1";
		case 0x1B : return "Eb1";
		case 0x1A : return "D1";
		case 0x19 : return "C#1";
		case 0x18 : return "C1";
		case 0x17 : return "B0";
		case 0x16 : return "Bb0";
		case 0x15 : return "A0";
		case 0x14 : return "Ab0";
		case 0x13 : return "G0";
		case 0x12 : return "F#0";
		case 0x11 : return "F0";
		case 0x10 : return "E0";
		case 0x0F : return "Eb0";
		case 0x0E : return "D0";
		case 0x0D : return "C#0";
		case 0x0C : return "C0";
		case 0x0B : return "B(-1)";
		case 0x0A : return "Bb(-1)";
		case 0x09 : return "A(-1)";
		case 0x08 : return "Ab(-1)";
		case 0x07 : return "G(-1)";
		case 0x06 : return "F#(-1)";
		case 0x05 : return "F(-1)";
		case 0x04 : return "E(-1)";
		case 0x03 : return "Eb(-1)";
		case 0x02 : return "D(-1)";
		case 0x01 : return "C#(-1)";
		case 0x00 : return "C(-1)";
		default : return "?";
		}
	}
}
