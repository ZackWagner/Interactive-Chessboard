import java.util.ArrayList;

public class BlackKnight extends Piece {
  public BlackKnight(float x, float y) {
    super(x, y, "BlackKnight.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = false;
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesBlackKnight(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckBlack(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesBlackKnight(this);
  }
}
