import java.util.ArrayList;

public class WhiteBishop extends Piece {
  public WhiteBishop(float x, float y) {
    super(x, y, "WhiteBishop.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = true;
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesWhiteBishop(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckWhite(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesWhiteBishop(this);

  }
}
