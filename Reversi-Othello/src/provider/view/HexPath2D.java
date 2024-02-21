package provider.view;

import java.awt.geom.Path2D;

/**
 * A Path2D extension that draws a pointy-top hexagon with the given side length.
 */
public class HexPath2D extends Path2D.Double {
  /**
   * Instantiates a new Hexagonal 2D path with the give side length.
   *
   * @param sideLength the length of each side of the hexagon. Diagonal sides are slightly adjusted
   *                   to connect the flat sides
   */
  public HexPath2D(int sideLength) {
    super();
    double angle = 30;
    for (int side = 0; side < 6; side++) {
      double x = sideLength * Math.cos(Math.toRadians(angle));
      double y = sideLength * Math.sin(Math.toRadians(angle));

      if (side == 0) {
        this.moveTo(x, y);
      } else {
        this.lineTo(x, y);
      }

      angle += 60;
    }

    closePath();
  }


}
