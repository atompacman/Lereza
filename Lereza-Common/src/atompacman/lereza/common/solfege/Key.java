package atompacman.lereza.common.solfege;

import atompacman.lereza.common.solfege.quality.ThirdQuality;

public class Key {
	
	private Tone tone;
	private ThirdQuality quality;
	
	
	public Key(Tone tone, ThirdQuality quality) {
		this.tone = tone;
		this.quality = quality;
	}
	
	public Tone getTone() {
		return tone;
	}
	
	public ThirdQuality getQuality() {
		return quality;
	}
}
