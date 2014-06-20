package atompacman.lereza.core.builder.builder;

import atompacman.lereza.core.container.piece.Piece;

public interface Builder {

	public Piece build(Class<? extends Piece> musicalForm);
}
