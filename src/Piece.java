import java.util.ArrayList;

/**
 * This class models moving thing objects. A moving thing is defined by its speed and to which direction it is facing.
 */
public class Piece extends Thing implements Clickable  {

  protected float lastLocationX;
  protected float lastLocationY;
  public ArrayList<Location> possibleMoves;
  public boolean isWhite;

  /**
   * Creates a new MovingThing and sets its speed, image file, and initial x and y position.
   * @param x x coordinate of moving thing
   * @param y y coordinate of moving thing
   * @param imageFileName image of new moving thing
   */
  public Piece(float x, float y, String imageFileName) {
    super(x,y,imageFileName);
    this.lastLocationX = x;
    this.lastLocationY = y;
  }


  /**
   * Draws this Piece at its current position.
   */
  public void draw() {
    processing.pushMatrix();
    processing.rotate(0.0f);
    processing.translate(x, y);
    processing.image(image(), 0.0f, 0.0f);
    processing.popMatrix();
  }

  @Override
  public void mousePressed() {

  }
  public void mousePressed(Piece excludedPiece) {

  }
}
