import java.util.ArrayList;

public class BlackQueen extends Piece {
  public BlackQueen(float x, float y) {
    super(x, y, "BlackQueen.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = false;
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesBlackQueen(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckBlack(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesBlackQueen(this);
  }
}
