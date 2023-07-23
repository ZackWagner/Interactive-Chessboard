import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents an ArrayList backed stack that contians possible moves for an associated piece
 */
public class MoveList implements Iterable<Location>{
  private Piece associatedPiece; // piece associated with this ove list
  private ArrayList<Location> data; // array list to store moves

  public MoveList(Piece associatedPiece) {
    this.associatedPiece = associatedPiece;
    data = new ArrayList<Location>();
  }
  public void add(Location l) {
    data.add(l);
  }

  public void clear() {
    data.clear();
  }

  @Override
  public Iterator iterator() {
    return new MoveListIterator(this.data);
  }
}
