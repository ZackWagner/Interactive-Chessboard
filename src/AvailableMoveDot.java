public class AvailableMoveDot extends Thing{
  public AvailableMoveDot(Location place) {
    super(place.x, place.y, "AvailableMoveDot.png");
  }

  @Override
  public boolean isMouseOver() {
    return processing.mouseX >= this.x - 30
        && processing.mouseX <= this.x + 30
        && processing.mouseY >= this.y - 30
        && processing.mouseY <= this.y + 30;
  }
}
