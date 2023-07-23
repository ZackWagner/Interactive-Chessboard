import java.util.ArrayList;

public class WhiteKnight extends Piece {
  public WhiteKnight(float x, float y) {
    super(x, y, "WhiteKnight.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = true;
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesWhiteKnight(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckWhite(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesWhiteKnight(this);
  }
}
