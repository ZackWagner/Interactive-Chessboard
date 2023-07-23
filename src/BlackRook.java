import java.util.ArrayList;

public class BlackRook extends Piece {

  public BlackRook(float x, float y) {
    super(x, y, "BlackRook.png");
    isWhite = false;
    this.possibleMoves = new ArrayList<Location>();
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesBlackRook(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckBlack(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesBlackRook(this);
  }
}
