:remote connect tinkerpop.server conf/remote.yaml session
:remote console

g.V().drop().iterate()

full_path_and_filename = "$BASE_DIR/chapter09/scripts/restaurant-review-network.json"

g.io(full_path_and_filename).
  with(IO.reader,IO.graphson).
  read().
  iterate()

