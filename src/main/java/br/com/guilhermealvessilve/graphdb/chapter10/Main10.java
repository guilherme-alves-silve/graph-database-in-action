package br.com.guilhermealvessilve.graphdb.chapter10;

import br.com.guilhermealvessilve.graphdb.chapter09.App09;
import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.identity;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;

public class Main10 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = getGraphTraversalSource(graph);
            calculateDegreeOfTop10Vertices(g);
        }
    }

    public static GraphTraversalSource getGraphTraversalSource(TinkerGraph graph) {
        var g = graph.traversal();
        g.V().drop().iterate();
        var fileName = "/chapter09/scripts/restaurant-review-network.json";
        var fullFileName = requireNonNull(App09.class.getResource(fileName)).toString()
                .replace("file:/", "");

        g.io(fullFileName)
                .with(IO.reader, IO.graphson)
                .read()
                .iterate();
        return g;
    }

    private static void calculateDegreeOfTop10Vertices(GraphTraversalSource g) {
        var result = g.V().project("vertex", "degree")
                .by(identity())
                .by(bothE().count())
                .order()
                .by(select("degree"), desc)
                .limit(10)
                .toList();
        System.out.println("Calculate degree of top 10 vertices: " + result.toString());
    }
}
