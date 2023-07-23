import java.io.File;
import processing.core.PImage;

/**
 * This class models a graphic Thing which can be drawn at a give
 * (x,y) position within the display window of a graphic application.
 */
public class Thing {

  private processing.core.PImage image; // image of this graphic thing of type PImage
  protected static processing.core.PApplet processing; // PApplet object that represents the display window of this graphic application
  public float x; // x-position of this thing in the display window
  public float y; // y-position of this thing in the display window

  /**
   * Creates a new graphic Thing located at a specific (x, y) position of the display window
   *
   * @param x x-coordinate of this thing in the display window
   * @param y y-coordinate of this thing in the display window
   * @param imageFilename filename of the image of this thing, for instance "name.png"
   */
  public Thing(float x, float y, String imageFilename) {
    // Set drawing parameters
    this.image = processing.loadImage("images" + File.separator + imageFilename);
    // sets the position of this decoration object
    this.x = x;
    this.y = y;

  }
  /**
   * Draws this thing to the display window at its current (x,y) position
   */
  public void draw() {
    // draw the thing at its current position
    processing.image(this.image, this.x, y);

  }
  /**
   * Returns a reference to the image of this thing
   *
   * @return the image of type PImage of the thing object
   */
  public PImage image() {
    return image;
  }

  /**
   * Checks if the mouse is over this badger
   *
   * @return true if the mouse is over this badger object, otherwise returns false.
   */
  public boolean isMouseOver() {
    int thingWidth = this.image().width;
    int thingHeight = this.image().height;

    // checks if the mouse is over this
    return processing.mouseX >= this.x - thingWidth / 2
        && processing.mouseX <= this.x + thingWidth / 2
        && processing.mouseY >= this.y - thingHeight / 2
        && processing.mouseY <= this.y + thingHeight / 2;
  }

  /**
   * Sets the PApplet object display window where this Thing object will be drawn
   * @param processing PApplet object that represents the display window
   */
  public static void setProcessing(processing.core.PApplet processing) {
    Thing.processing = processing;
  }


}
