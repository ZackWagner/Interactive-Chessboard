import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MoveListIterator implements Iterator<Location> {
  private Location next; // The TweetNode containing the next tweet to be returned in the iteration
  private ArrayList<Location> data;

  /**
   * Constructs a new twiterator at the given starting node
   */
  public MoveListIterator( ArrayList<Location> data) {
    this.data = data;
    if (data.isEmpty()) {
      this.next = null;
    }
    else {
      this.next = this.data.get(1);
    }
  }

  /**
   * Checks whether there is a next tweet to return
   * @return true if there is a next tweet, false if the value of next is null
   */
  @Override
  public boolean hasNext() {
    return (this.next != null);
  }

  /**
   * Returns the next tweet in the iteration if one exists, and advances next to the next tweet
   * @return the next tweet in the iteration if one exists
   */
  @Override
  public Location next() {
    if (this.next == null) throw new NoSuchElementException();
    Location temp = this.next;
    if (data.indexOf(temp)+1 < data.size()) {
      this.next = data.get(data.indexOf(temp) + 1);
    }
    else {
      this.next = null;
    }
    return temp;
  }
}
