
/**
 * This interface models Clickable objects in a graphic application
 *
 */
public interface Clickable {

  /**
   * Implements the behavior to be run each time the mouse is pressed.
   */
  public void mousePressed();
  public void mousePressed(Piece excludedPiece);



}
