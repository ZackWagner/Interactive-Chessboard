import java.util.ArrayList;

public class BlackKing extends Piece {
  public BlackKing(float x, float y) {
    super(x, y, "BlackKing.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = false;
  }
  @Override
  public void mousePressed() {
    ChessGame.calculatePossibleMovesBlackKing(this); }
}
