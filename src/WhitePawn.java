import java.util.ArrayList;

public class WhitePawn extends Piece {

  public boolean hasMoved;

  public WhitePawn(float x, float y) {
    super(x, y, "WhitePawn.png");
    this.hasMoved = false;
    this.possibleMoves = new ArrayList<Location>();
    isWhite = true;
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesWhitePawn(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckWhite(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesWhitePawn(this);

  }
}
