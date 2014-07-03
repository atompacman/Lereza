package atompacman.lereza.common.solfege;

public class Genre {
	
	private Subgenre subGenre;
	
	
	//////////////////////////////
	//       CONSTRUCTOR        //
	//////////////////////////////
	
	public Genre(Subgenre subGenre) {
		this.subGenre = subGenre;
	}
	
	
	//////////////////////////////
	//         GETTERS         //
	//////////////////////////////
	
	public BroadGenre getBroadGenre() {
		return BroadGenre.valueOf(BroadGenre.class, subGenre.getClass().getName());
	}
	
	public Subgenre getSubGenre() {
		return subGenre;
	}
	
	
	//////////////////////////////
	//        SUBGENRES         //
	//////////////////////////////
	
	public enum BroadGenre {
		BAROQUE, METAL;
	}
	
	
	public interface Subgenre {}
	
	
	public enum BAROQUE implements Subgenre {
		EARLY_BAROQUE, 
		HIGH_BAROQUE, 
		MIDDLE_BAROQUE,
	}
	
	public enum METAL implements Subgenre {
		BLACK_METAL, 
		DEATH_METAL, 
		DJENT, 
		HAIR_METAL, 
		HARDCORE, 
		MATHCORE, 
		MELODIC_DEATH_METAL, 
		METALCORE, 
		NU_METAL,	
		PROGRESSIVE_METAL, 
		THRASH_METAL, 
	}
}
