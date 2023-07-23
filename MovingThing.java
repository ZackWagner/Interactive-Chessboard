/**
 * This class models moving thing objects. A moving thing is defined by its speed and to which direction it is facing.
 */
public class MovingThing extends Thing implements Comparable<MovingThing> {

  protected boolean isFacingRight; // indicates whether this MovingThing is facing right or not
  protected int speed; // movement speed of this MovingThing

  /**
   * Creates a new MovingThing and sets its speed, image file, and initial x and y position.
   * @param x x coordinate of moving thing
   * @param y y coordinate of moving thing
   * @param speed speed of new moving thing
   * @param imageFileName image of new moving thing
   */
  public MovingThing(float x, float y, int speed, String imageFileName) {
    super(x,y,imageFileName);
    this.speed = speed;
    this.isFacingRight = true;
  }

  /**
   * Compares this object with the specified MovingThing for order, in the increasing order of their speeds.
   * @param other the object to be compared.
   * @return positive number if other has a lower speed than the thing being called, 0 if both speeds are equal, positive number otherwise
   */
  @Override
  public int compareTo(MovingThing other) {
    return this.speed-other.speed;
  }

  /**
   * Draws this MovingThing at its current position.
   */
  public void draw() {
    processing.pushMatrix();
    processing.rotate(0.0f);
    processing.translate(x, y);
    if (!isFacingRight) {
      processing.scale(-1.0f, 1.0f);
    }
    processing.image(image(), 0.0f, 0.0f);
    processing.popMatrix();
  }

}

