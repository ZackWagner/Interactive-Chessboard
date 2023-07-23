import java.util.ArrayList;

public class BlackPawn extends Piece {
  public boolean hasMoved;
  public BlackPawn(float x, float y) {
    super(x, y, "BlackPawn.png");
    isWhite = false;
    this.possibleMoves = new ArrayList<Location>();
  }
  public void mousePressed() {
    ChessGame.calculatePossibleMovesBlackPawn(this);
    for (int i = 0; i<possibleMoves.size(); i++) {
      if (ChessGame.movedToCheckBlack(this, possibleMoves.get(i))) {
        possibleMoves.remove(i);
        i--;
      }
    }
  }
  public void mousePressed(Piece excludedPiece) {
    ChessGame.calculatePossibleMovesBlackPawn(this);
  }
}
