public class EnPassantMoveDot extends AvailableMoveDot {
  public EnPassantMoveDot(float x, float y) {
    super(new Location(x, y));
  }

  @Override
  public boolean isMouseOver() {
    return processing.mouseX >= this.x - 30
        && processing.mouseX <= this.x + 30
        && processing.mouseY >= this.y - 30
        && processing.mouseY <= this.y + 30;
  }
}

