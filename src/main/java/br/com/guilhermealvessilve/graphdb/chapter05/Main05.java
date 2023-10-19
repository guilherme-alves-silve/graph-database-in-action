package br.com.guilhermealvessilve.graphdb.chapter05;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class Main05 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = graph.traversal();
            initialize(g);
            friendsAndFriendsOfFriends(g);
        }
    }

    private static void friendsAndFriendsOfFriends(GraphTraversalSource g) {
        var result = g.V().has("person", "first_name", "Dave")
                .out().as("f")
                .out().as("fof")
                .select("f", "fof")
                .by("first_name")
                .by("first_name");
        System.out.println("friendsAndFriendsOfFriends:");
        while (result.hasNext()) {
            System.out.println(result.next());
        }
    }

    private static void initialize(GraphTraversalSource g) {
        g.V().drop();
        var dave = g.addV("person").property("first_name", "Dave").next();
        var josh = g.addV("person").property("first_name", "Josh").next();
        var ted = g.addV("person").property("first_name", "Ted").next();
        var hank = g.addV("person").property("first_name", "Hank").next();
        var kelly = g.addV("person").property("first_name", "Kelly").next();
        var jim = g.addV("person").property("first_name", "Jim").next();
        var paras = g.addV("person").property("first_name", "Paras").next();
        var denise = g.addV("person").property("first_name", "Denise").next();

        g.addE("friends").from(dave).to(ted)
                .addE("friends").from(dave).to(josh)
                .addE("friends").from(dave).to(hank)
                .addE("friends").from(josh).to(hank)
                .addE("friends").from(ted).to(josh).iterate();

        g.addE("friends").from(dave).to(jim)
                .addE("friends").from(dave).to(kelly)
                .addE("friends").from(kelly).to(jim)
                .addE("friends").from(kelly).to(denise)
                .addE("friends").from(jim).to(denise)
                .addE("friends").from(jim).to(paras)
                .addE("friends").from(paras).to(denise)
                .iterate();
    }
}
