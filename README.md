# Graph Database in Action Study

Study of the book [Graph Database in Action](https://www.manning.com/books/graph-databases-in-action)

## Run ArcadeDB with Gremlin

``
docker run --rm  -p 2480:2480 -p 2424:2424 -p 6379:6379 -p 5432:5432 -p 8182:8182 \
    --env JAVA_OPTS="-Darcadedb.server.rootPassword=playwithdata \
    -Darcadedb.server.defaultDatabases=Imported[root]{import:https://github.com/ArcadeData/arcadedb-datasets/raw/main/orientdb/OpenBeer.gz} \
    -Darcadedb.server.plugins=Redis:com.arcadedb.redis.RedisProtocolPlugin,MongoDB:com.arcadedb.mongo.MongoDBProtocolPlugin,Postgres:com.arcadedb.postgres.PostgresProtocolPlugin,GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin" \
arcadedata/arcadedb:latest
``