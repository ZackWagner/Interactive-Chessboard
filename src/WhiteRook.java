import java.util.ArrayList;

public class WhiteRook extends Piece {
  public WhiteRook(float x, float y) {
    super(x, y, "WhiteRook.png");
    isWhite = true;
    this.possibleMoves = new ArrayList<Location>();
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesWhiteRook(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckWhite(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesWhiteRook(this);
  }
}
