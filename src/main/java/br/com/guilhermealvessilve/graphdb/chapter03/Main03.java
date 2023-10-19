package br.com.guilhermealvessilve.graphdb.chapter03;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.has;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;

public class Main03 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = graph.traversal();
            initialize(g);
            var ted = g.V().hasLabel("person")
                    .has("first_name", "Ted")
                    .next();
            System.out.println(ted.properties().next());

            var josh = g.V(ted).out("friends").next();
            System.out.println("Ted out friends: " + josh.properties().next());

            var dave = g.V(ted).in("friends").next();
            System.out.println("Ted in friends: " + dave.properties().next());

            var bothFriends = g.V(ted).both("friends")
                    .values().fold().next();
            System.out.println(bothFriends);

            var hank1 = g.V(ted).out().out()
                    .properties().next();
            System.out.println("Simple search: " + hank1);

            var hank2 = g.V(ted)
                    .repeat(out())
                    .until(has("person", "first_name", "Hank"))
                    .properties()
                    .next();
            System.out.println("Using repeat and until: " + hank2);

            var hank3 = g.V(ted)
                    .repeat(out())
                    .times(2)
                    .properties()
                    .next();

            System.out.println("Using repeat and times: " + hank3);

            var allNamesToHank1 = g.V().hasLabel("person").has("first_name", "Ted")
                    .emit()
                    .until(has("person", "first_name", "Hank"))
                    .repeat(out())
                    .values()
                    .fold()
                    .next();
            System.out.println("allNamesToHank1: " + allNamesToHank1); // [Ted, Josh, Hank]

            var allNamesToHank2 = g.V().has("person", "first_name", "Ted")
                    .until(has("person", "first_name", "Hank"))
                    .repeat(out())
                    .emit()
                    .values()
                    .fold()
                    .next();
            System.out.println("allNamesToHank2: " + allNamesToHank2); // [Josh, Hank, Hank]

            var allNamesToHank3 = g.V().has("person", "first_name", "Ted")
                    .repeat(out())
                    .until(has("person", "first_name", "Hank"))
                    .emit()
                    .values()
                    .fold()
                    .next();
            System.out.println("allNamesToHank3: " + allNamesToHank3); // [Josh, Hank]
        }
    }

    private static void initialize(GraphTraversalSource g) {
        g.V().drop();
        var dave = g.addV("person").property("first_name", "Dave").next();
        var josh = g.addV("person").property("first_name", "Josh").next();
        var ted = g.addV("person").property("first_name", "Ted").next();
        var hank = g.addV("person").property("first_name", "Hank").next();

        g.addE("friends").from(dave).to(ted).next();
        g.addE("friends").from(dave).to(josh).next();
        g.addE("friends").from(dave).to(hank).next();
        g.addE("friends").from(josh).to(hank).next();
        g.addE("friends").from(ted).to(josh).next();
    }
}
