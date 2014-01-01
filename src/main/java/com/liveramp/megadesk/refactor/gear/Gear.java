package com.liveramp.megadesk.refactor.gear;

import java.util.List;

import com.liveramp.megadesk.refactor.attempt.Outcome;
import com.liveramp.megadesk.refactor.node.Node;

public interface Gear {

  Node getNode();

  List<Node> reads();

  List<Node> writes();

  boolean isRunnable();

  Outcome run() throws Exception;
}
