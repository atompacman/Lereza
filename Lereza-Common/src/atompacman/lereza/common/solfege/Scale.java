package atompacman.lereza.common.solfege;

public class Scale {

	private Tone tone;
	private ScaleType type;
	
	
	public Scale(Tone tone, ScaleType type) {
		this.tone = tone;
		this.type = type;
	}
	
	public Tone getTone() {
		return tone;
	}
	
	public ScaleType getType() {
		return type;
	}
}
