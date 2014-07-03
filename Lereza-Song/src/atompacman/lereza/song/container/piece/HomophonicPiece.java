package atompacman.lereza.song.container.piece;

import java.util.ArrayList;
import java.util.List;

import atompacman.lereza.song.container.part.Hand;

public class HomophonicPiece extends Piece {
	
	protected List<Hand> hands;
	
	
	//////////////////////////////
	//       CONSTRUCTORS       //
	//////////////////////////////
	
	public HomophonicPiece() {
		super();
		this.hands = new ArrayList<Hand>();
	}
	
	
	//////////////////////////////
	//         SETTERS          //
	//////////////////////////////
	
	public void addPart(Hand hand) {
		hands.add(hand);
	}
	
	
	//////////////////////////////
	//         GETTERS          //
	//////////////////////////////
	
	public Hand getHand(int index) {
		return hands.get(index);
	}
}
