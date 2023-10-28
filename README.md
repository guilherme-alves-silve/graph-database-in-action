# Graph Database in Action Study

- [ ] Study of the book [Graph Database in Action](https://www.manning.com/books/graph-databases-in-action)
- [ ] Study of the ebook [PRACTICAL GREMLIN: An Apache TinkerPop Tutorial](https://kelvinlawrence.net/book/Gremlin-Graph-Guide.html)
- [ ] Study about working with OpenStreetMap and Leaflet
  - [OpenStreetMap Data Extracts](http://download.geofabrik.de/)
    - [Way](https://wiki.openstreetmap.org/wiki/Way)
    - [Directions](https://wiki.openstreetmap.org/wiki/Forward_%26_backward,_left_%26_right)
  - [Neo4j Tutorial](https://neo4j.com/developer-blog/routing-web-app-neo4j-openstreetmap-leafletjs/)
    - Used as reference
  - [OSM2PO](http://osm2po.de/)
    - Used as reference
- [ ] Study about working with ArcadeDB and OpenCLIP

## Libraries
 
- [Apache TinkerPop](https://tinkerpop.apache.org/)
- [ArcadeDB](https://arcadedb.com/)
- [JanusGraph](https://janusgraph.org/)
- [Deep Java Library](https://djl.ai/)
- [WebJars](https://www.webjars.org)
- [Leaflet](https://leafletjs.com/)

## Run ArcadeDB with Gremlin

Bash:
``
docker run --rm  -p 2480:2480 -p 2424:2424 -p 6379:6379 -p 5432:5432 -p 8182:8182 \
    --env JAVA_OPTS="-Darcadedb.server.rootPassword=rootroot \
    -Darcadedb.server.defaultDatabases=Imported[root]{import:https://github.com/ArcadeData/arcadedb-datasets/raw/main/orientdb/OpenBeer.gz} \
    -Darcadedb.server.plugins=GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin" \
arcadedata/arcadedb:latest
``

Or CMD without import:
``
docker run --rm  -p 2480:2480 -p 2424:2424 -p 6379:6379 -p 5432:5432 -p 8182:8182 --env JAVA_OPTS="-Darcadedb.server.rootPassword=rootroot -Darcadedb.server.plugins=GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin" arcadedata/arcadedb:latest
``
