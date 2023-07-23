import java.util.ArrayList;

public class BlackBishop extends Piece {
  public BlackBishop(float x, float y) {
    super(x, y, "BlackBishop.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = false;
  }
  @Override
  public void mousePressed() {
    ChessGame.calculatePossibleMovesBlackBishop(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckBlack(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesBlackBishop(this);
  }
}
