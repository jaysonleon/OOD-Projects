package provider.model;

/**
 * A value class for a coordinate on a hexagonal grid.
 */
public class HexCoordinate {
  // the Q axis value (left to right on a flat-top hexagon)
  public final int q;
  // the R axis value (top left to bottom right on a flat-top hexagon)
  public final int r;
  // the S axis value (top right to bottom left on a flat-top hexagon)
  public final int s;

  /**
   * Instantiates a new hexagonal coordinate. The three given values must add to 0 as that is how
   * the coordinate system maintains consistency given S is a derived axis.
   *
   * @param q the Q axis value
   * @param r the R axis value
   * @param s the S axis value
   * @throws IllegalArgumentException if the three given coordinates do not add to 0
   * @implNote Class invariant: the Q, R, and S values must always add to 0
   */
  public HexCoordinate(int q, int r, int s) throws IllegalArgumentException {
    if (q + r + s != 0) {
      throw new IllegalArgumentException("Hexagon coordinates must equal to 0 to be valid");
    }

    this.q = q;
    this.r = r;
    this.s = s;
  }

  @Override
  public String toString() {
    return "q: " + q + ", r: " + r + ", s: " + s;
  }

  /**
   * A given object is equal to this HexCoordinate is it's Q, R, and S fields are identical.
   *
   * @param o the object to compare to
   * @return if the given object is equal to this HexCoordinate
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof HexCoordinate)) {
      return false;
    }
    if (o == this) {
      return true;
    } else {
      return (((HexCoordinate) o).q == this.q)
             && (((HexCoordinate) o).r == this.r)
             && (((HexCoordinate) o).s == this.s);
    }
  }

  @Override
  public int hashCode() {
    return (this.q * 23) + r * s;
  }

}
