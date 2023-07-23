import java.util.ArrayList;

public class WhiteQueen extends Piece {
  public WhiteQueen(float x, float y) {
    super(x, y, "WhiteQueen.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = true;
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesWhiteQueen(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckWhite(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesWhiteQueen(this);
  }
}
