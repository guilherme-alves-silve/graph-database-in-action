package br.com.guilhermealvessilve.graphdb.chapter04;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.process.traversal.P.lte;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.*;

public class Main04 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = graph.traversal();
            initialize(g);
            tedToDenisePath(g);
            workedWithBefore2018(g);
            workedWith1(g);
            workedWith2(g);
            tedToDenisePath2(g);
            countEdges(g);
        }
    }

    private static void countEdges(GraphTraversalSource g) {
        System.out.println("count 1: " + g.V().bothE().count().next());
        System.out.println("count 2: " + g.V().both().count().next());
    }

    private static void workedWith1(GraphTraversalSource g) {
        var workedWith = g.V().has("person", "first_name", "Dave")
                .bothE("works_with")
                .otherV()
                .values()
                .fold()
                .next();
        System.out.println("workedWith1: " + workedWith);
    }

    private static void workedWith2(GraphTraversalSource g) {
        var workedWith = g.V().has("person", "first_name", "Dave")
                .both("works_with")
                .values()
                .fold()
                .next();
        System.out.println("workedWith2: " + workedWith);
    }

    private static void tedToDenisePath(GraphTraversalSource g) {
        var traversal = g.V().has("person", "first_name", "Ted")
                .until(has("person", "first_name", "Denise"))
                .repeat(both().simplePath())
                .path();
        int i = 0;
        while (traversal.hasNext()) {
            var tedPathDenise = traversal.next();
            System.out.println("tedPathDenise" + ++i  + ": " + tedPathDenise);
        }
    }

    private static void tedToDenisePath2(GraphTraversalSource g) {
        var traversal = g.V().has("person", "first_name", "Ted")
                .until(has("person", "first_name", "Denise"))
                .repeat(bothE("works_with").otherV().simplePath())
                .path();
        int i = 0;
        while (traversal.hasNext()) {
            var tedPathDenise = traversal.next();
            System.out.println("tedToDenisePath2" + ++i  + ": " + tedPathDenise);
        }
    }

    /**
     * "Who did Dave work with before the job he started in 2018?"
     */
    private static void workedWithBefore2018(GraphTraversalSource g) {
        var workedWithBefore2018 = g.V().has("person", "first_name", "Dave")
                .bothE("works_with").has("end_year", lte(2018))
                .otherV()
                .values("first_name")
                .fold()
                .next();
        System.out.println("workedWithBefore2018: " + workedWithBefore2018);
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

        g.addE("works_with").from(dave).to(josh)
                .property("start_year",2016).property("end_year",2017)
                .addE("works_with").from(dave).to(ted)
                .property("start_year",2016).property("end_year",2017)
                .addE("works_with").from(josh).to(ted)
                .property("start_year",2016).property("end_year",2019)
                .addE("works_with").from(dave).to(hank)
                .property("start_year",2017).property("end_year",2018)
                .addE("works_with").from(dave).to(kelly)
                .property("start_year",2018)
                .addE("works_with").from(dave).to(denise)
                .property("start_year",2018)
                .addE("works_with").from(denise).to(kelly)
                .property("start_year",2018)
                .iterate();
    }
}
