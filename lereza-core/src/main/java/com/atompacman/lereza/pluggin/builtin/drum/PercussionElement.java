package com.atompacman.lereza.pluggin.builtin.drum;

public enum PercussionElement {

    // BASS DRUM
    BASS_DRUM_1         (PercussionType.BASS_DRUM,  36),
    BASS_DRUM_2         (PercussionType.BASS_DRUM,  35),
    
    //SNARE
    SNARE_DRUM_1        (PercussionType.SNARE,      38),
    SNARE_DRUM_2        (PercussionType.SNARE,      40),
    HAND_CLAP           (PercussionType.SNARE,      39),
    TAMBOURINE          (PercussionType.SNARE,      54),
    
    // HI-HAT
    OPEN_HI_HAT         (PercussionType.HI_HAT,     46),
    CLOSED_HI_HAT       (PercussionType.HI_HAT,     42),
    PEDAL_HI_HAT        (PercussionType.HI_HAT,     44),

    // TOM
    LOW_TOM_1           (PercussionType.TOM,        43),
    LOW_TOM_2           (PercussionType.TOM,        41),
    MID_TOM_1           (PercussionType.TOM,        47),
    MID_TOM_2           (PercussionType.TOM,        45),
    HIGH_TOM_1          (PercussionType.TOM,        50),
    HIGH_TOM_2          (PercussionType.TOM,        48),
    
    // CRASH
    CRASH_CYMBAL_1      (PercussionType.CRASH,      49),
    CRASH_CYMBAL_2      (PercussionType.CRASH,      57),
    CHINESE_CYMBAL_1    (PercussionType.CRASH,      52),
    SPLASH_CYMBAL_1     (PercussionType.CRASH,      55),

    // RIDE
    RIDE_CYMBAL_1       (PercussionType.RIDE,       51),
    RIDE_CYMBAL_2       (PercussionType.RIDE,       59),
    RIDE_BELL           (PercussionType.RIDE,       53),

    // AFRO-LATIN
    LOW_BONGO           (PercussionType.AFRO_LATIN, 61),
    HIGH_BONGO          (PercussionType.AFRO_LATIN, 60),
    LOW_CONGA           (PercussionType.AFRO_LATIN, 64),
    OPEN_HIGH_CONGA     (PercussionType.AFRO_LATIN, 63),
    MUTE_HIGH_CONGA     (PercussionType.AFRO_LATIN, 62),
    LOW_TIMBALE         (PercussionType.AFRO_LATIN, 66),
    HIGH_TIMBALE        (PercussionType.AFRO_LATIN, 65),
    LOW_AGOGO           (PercussionType.AFRO_LATIN, 68),
    HIGH_AGOGO          (PercussionType.AFRO_LATIN, 67),
    SHORT_GUIRO         (PercussionType.AFRO_LATIN, 73),
    LONG_GUIRO          (PercussionType.AFRO_LATIN, 74),
    OPEN_CUICA          (PercussionType.AFRO_LATIN, 79),
    MUTE_CUICA          (PercussionType.AFRO_LATIN, 78),
    CABASA              (PercussionType.AFRO_LATIN, 69),
    MARACAS             (PercussionType.AFRO_LATIN, 70),
    CLAVES              (PercussionType.AFRO_LATIN, 75),
    
    // OTHER
    SIDE_STICK          (PercussionType.OTHER,      37),
    COW_BELL            (PercussionType.OTHER,      56),
    VIBRA_SLAP          (PercussionType.OTHER,      58),
    SHORT_WHISLE        (PercussionType.OTHER,      71),
    LONG_WHISLE         (PercussionType.OTHER,      72),
    LOW_WOOD_BLOCK      (PercussionType.OTHER,      77),
    HIGH_WOOD_BLOCK     (PercussionType.OTHER,      76),
    OPEN_TRIANGLE       (PercussionType.OTHER,      81),
    MUTE_TRIANGLE       (PercussionType.OTHER,      80),
    
    // UNKNOWN
    UNKNOWN             (null,                       0);
    
    
    //==================================== STATIC FIELDS =========================================\\

    public static PercussionElement[] HEX_NOTE_TO_PERCUSSION;
    
    
    
    //======================================= FIELDS =============================================\\

    private PercussionType type;
    private byte           hexNote;
    


    //======================================= METHODS ============================================\\

    //--------------------------------- STATIC INITIALIZATION ------------------------------------\\

    public static PercussionElement withHexNote(byte hexNote) {
        // Create list if not already created
        if (HEX_NOTE_TO_PERCUSSION == null) {
            HEX_NOTE_TO_PERCUSSION = new PercussionElement[256];
            for (PercussionElement elem : values()) {
                HEX_NOTE_TO_PERCUSSION[elem.hexNote] = elem;
            }
        }
        PercussionElement elem = HEX_NOTE_TO_PERCUSSION[hexNote];
        return elem == null ? UNKNOWN : elem;
    }
    
    
    //---------------------------------- PRIVATE CONSTRUCTOR -------------------------------------\\

    private PercussionElement(PercussionType type, int hexNote) {
        this.type = type;
        if (hexNote != (byte) hexNote) {
            throw new IllegalArgumentException();
        }
        this.hexNote = (byte) hexNote;
    }


    //--------------------------------------- GETTERS --------------------------------------------\\

    public PercussionType getType() {
        return type;
    }

    public byte getHexNote() {
        return hexNote;
    }
}
