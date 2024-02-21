package adapter;

import java.awt.Point;
import java.util.List;
import java.util.Optional;

import model.player.Player;
import model.model.ReadonlyReversiModel;
import strategy.FallibleReversiStrategy;
import provider.model.HexCoordinate;
import provider.model.PlayerSymbol;
import provider.strategy.ReversiStrategy;

/**
 * Represents the provided strategy interface adapted to our strategy interface.
 */
public class ProvStratToOurStratAdapter implements FallibleReversiStrategy {
  private final ReversiStrategy adaptee;

  /**
   * Constructs a new ProvStratToOurStratAdapter.
   * @param strategy the strategy to adapt (provided interface)
   */
  public ProvStratToOurStratAdapter(ReversiStrategy strategy) {
    this.adaptee = strategy;
  }

  @Override
  public Optional<Point> chooseMove(ReadonlyReversiModel model, Player player)
          throws IllegalStateException {
    List<HexCoordinate> hexCoordinate;
    if (player == Player.BLACK) {
      hexCoordinate = adaptee.chooseHexCoord(new ModelToProvModelAdapter(model),
              PlayerSymbol.BLACK);
    } else {
      hexCoordinate = adaptee.chooseHexCoord(new ModelToProvModelAdapter(model),
              PlayerSymbol.WHITE);
    }
    if (hexCoordinate.isEmpty()) {
      return Optional.empty();
    } else {
      int diff = model.getSideLen() - 1;
      return Optional.of(new Point(hexCoordinate.get(0).q + diff, hexCoordinate.get(0).r + diff));
    }
  }
}
