package br.com.guilhermealvessilve.graphdb.chapter07;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.count;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;
import static org.apache.tinkerpop.gremlin.structure.Column.values;

public class Main07 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = graph.traversal();
            initializeSpecific(g);
            initializeGenericVertexSpecificEdges(g);
            initializeGeneric(g);
            getPhoneNumbersContactsSpecific(g);
            getContactsSpecific(g);
            getContactsGenericVertexSpecificEdges(g);
            getContactsGeneric(g);
        }
    }

    private static void getPhoneNumbersContactsSpecific(GraphTraversalSource g) {
        var result = g.V().has("person", "name", "Ted")
                .out("has_phone")
                .values("number")
                .toList();
        System.out.println("getContactsOfTed1: " + result);
    }

    @SuppressWarnings("unchecked")
    private static void getContactsSpecific(GraphTraversalSource g) {
        var result = g.V().has("person", "name", "Ted")
            .union(
                out("has_phone").hasLabel("phone").values("number"),
                out("has_email").hasLabel("email").values("address"),
                out("has_fax").hasLabel("fax").values("number")
            )
            .toList();
        System.out.println("getContactsSpecific (Ted): " + result);

        var result2 = g.V()
                .union(
                        out("has_phone").hasLabel("phone").values("number"),
                        out("has_email").hasLabel("email").values("address"),
                        out("has_fax").hasLabel("fax").values("number")
                )
                .toList();
        System.out.println("getContactsSpecific (All): " + result2);
    }

    @SuppressWarnings("unchecked")
    private static void getContactsGenericVertexSpecificEdges(GraphTraversalSource g) {
        var result = g.V().has("person", "name", "Ted")
                .union(
                        out("has_phone").hasLabel("phone").values("number"),
                        out("has_email").hasLabel("email").values("address"),
                        out("has_fax").hasLabel("fax").values("number")
                )
                .toList();
        System.out.println("getContactsGenericVertexSpecificEdges (Ted): " + result);

        var result2 = g.V().hasLabel("contact")
                        .values("type", "number", "address")
                        .toList();
        System.out.println("getContactsGenericVertexSpecificEdges: " + result2);
    }

    private static void getContactsGeneric(GraphTraversalSource g) {
        var result = g.V().has("person", "name", "Ted")
                .out("contact_by")
                .values("type", "number", "address")
                .toList();
        System.out.println("getContactsGeneric (Ted): " + result);

        var result2 = g.V().hasLabel("contact")
                .values("type", "number", "address")
                .toList();
        System.out.println("getContactsGeneric (All): " + result2);
    }

    private static void initializeSpecific(GraphTraversalSource g) {
        g.V().drop();
        var ted = g.addV("person").property("name", "Ted").next();

        var phone = g.addV("phone").property("number", "555-1212").next();
        var email = g.addV("email").property("address", "fake@fake.com").next();
        var fax = g.addV("fax").property("number", "555-1213").next();

        g.addE("has_phone").from(ted).to(phone).next();
        g.addE("has_email").from(ted).to(email).next();
        g.addE("has_fax").from(ted).to(fax).next();
    }

    private static void initializeGenericVertexSpecificEdges(GraphTraversalSource g) {
        var ted = g.V().has("person", "name", "Ted")
                .next();
        var phone = g.addV("contact")
                .property("number", "555-1212")
                .property("type", "phone").next();
        var email = g.addV("contact")
                .property("address", "fake@fake.com")
                .property("type", "email").next();
        var fax = g.addV("contact")
                .property("number", "555-1213")
                .property("type", "fax").next();

        g.addE("has_phone").from(ted).to(phone).next();
        g.addE("has_email").from(ted).to(email).next();
        g.addE("has_fax").from(ted).to(fax).next();
    }

    private static void initializeGeneric(GraphTraversalSource g) {
        var ted = g.V().has("person", "name", "Ted").next();
        var phone = g.V().has("contact", "number", "555-1212").next();
        var email = g.V().has("contact", "address", "fake@fake.com").next();
        var fax = g.V().has("contact", "number", "555-1213").next();

        g.addE("contact_by").from(ted).to(phone).next();
        g.addE("contact_by").from(ted).to(email).next();
        g.addE("contact_by").from(ted).to(fax).next();
    }
}
