package br.com.guilhermealvessilve.graphdb.chapter05;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.shuffle;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.bothE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.count;
import static org.apache.tinkerpop.gremlin.structure.Column.keys;
import static org.apache.tinkerpop.gremlin.structure.Column.values;

public class Main05 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = graph.traversal();
            initialize(g);
            friendsAndFriendsOfFriends(g);
            numberOfFriendsOfEachPerson(g);
            orderByNameAsc(g);
            orderByNameDesc(g);
            orderByNameShuffle(g);
            groupFriendsOfFriends(g);
            groupFriendsOfFriendsCount(g);
            groupFriendsOfFriendsCount2(g);
            getFirst3Friends(g);
            getLast3Friends(g);
            getLast3FriendsReverse(g);
            getRange4Friends(g);
            getTop3FriendsOfFriendsByEdgeCount(g);
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

    private static void numberOfFriendsOfEachPerson(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .project("name", "degree")
                .by("first_name")
                .by(bothE().count());
        System.out.println("numberOfFriendsOfEachPerson:");
        result.forEachRemaining(System.out::println);
    }

    private static void orderByNameAsc(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .by();
        System.out.println("orderByNameAsc:");
        result.forEachRemaining(System.out::println);
    }

    private static void orderByNameDesc(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .by(desc);
        System.out.println("orderByNameDesc:");
        result.forEachRemaining(System.out::println);
    }

    private static void orderByNameShuffle(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .by(shuffle);
        System.out.println("orderByNameShuffle:");
        result.forEachRemaining(System.out::println);
    }

    private static void groupFriendsOfFriends(GraphTraversalSource g) {
        var result = g.V().has("person", "first_name", "Dave")
                .both()
                .both()
                .group()
                .by("first_name")
                .unfold();
        System.out.println("groupFriendsOfFriends:");
        result.forEachRemaining(System.out::println);
    }

    private static void groupFriendsOfFriendsCount(GraphTraversalSource g) {
        var result = g.V().has("person", "first_name", "Dave")
                .both()
                .both()
                .groupCount()
                .by("first_name")
                .unfold();
        System.out.println("groupFriendsOfFriendsCount:");
        result.forEachRemaining(System.out::println);
    }

    private static void groupFriendsOfFriendsCount2(GraphTraversalSource g) {
        var result = g.V().has("person", "first_name", "Dave")
                .both()
                .both()
                .group()
                .by("first_name")
                .by(count())
                .unfold();
        System.out.println("groupFriendsOfFriendsCount2:");
        result.forEachRemaining(System.out::println);
    }

    private static void getFirst3Friends(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .limit(3);
        System.out.println("getFirst3Friends:");
        result.forEachRemaining(System.out::println);
    }

    private static void getLast3Friends(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .tail(3);
        System.out.println("getLast3Friends:");
        result.forEachRemaining(System.out::println);
    }

    private static void getLast3FriendsReverse(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .by(desc)
                .tail(3);
        System.out.println("getLast3Friends:");
        result.forEachRemaining(System.out::println);
    }

    private static void getRange4Friends(GraphTraversalSource g) {
        var result = g.V().hasLabel("person")
                .values("first_name")
                .order()
                .range(0, 4);
        System.out.println("getRange4Friends:");
        result.forEachRemaining(System.out::println);
    }

    private static void getTop3FriendsOfFriendsByEdgeCount(GraphTraversalSource g) {
        var result = g.V().has("person", "first_name", "Dave")
                .both()
                .both()
                .groupCount()
                .by("first_name")
                .unfold()
                .order()
                .by(values, desc)
                .by(keys)
                .project("name", "count")
                .by(keys)
                .by(values)
                .limit(3);
        System.out.println("getTop3FriendsOfFriendsByEdgeCount:");
        result.forEachRemaining(System.out::println);
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
                .addE("friends").from(ted).to(josh)
                .iterate();

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
