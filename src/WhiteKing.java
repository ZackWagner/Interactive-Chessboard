import java.util.ArrayList;

public class WhiteKing extends Piece {
  public WhiteKing(float x, float y) {
    super(x, y, "WhiteKing.png");
    this.possibleMoves = new ArrayList<Location>();
    this.isWhite = true;
  }
   @Override
  public void mousePressed() {
   ChessGame.calculatePossibleMovesWhiteKing(this);
   }
}
