package br.com.guilhermealvessilve.graphdb.chapter06;

import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;

import java.util.Scanner;

import static org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.V;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.both;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.drop;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.has;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;

public class App06 {

    public static void main( String[] args ) {
        var cluster = connectToDatabase();
        try {
            var g = getGraphTraversalSource(cluster);
            displayMenu(g);
        } finally {
            cluster.close();
        }
        System.exit(0);
    }

    public static void displayMenu(GraphTraversalSource g) {
        int option = -1;
        while (option != 0) {
            option = showMenu();
            switch (option) {
                case 0:
                    break;
                case 1:
                    System.out.println("Person Vertex: " + getPerson(g));
                    break;
                case 2:
                    System.out.println("New person Vertex: " + addPerson(g));
                    break;
                case 3:
                    System.out.println("Update person Vertex: " + updatePerson(g));
                    break;
                case 4:
                    System.out.println("Delete person Vertex: " + deletePerson(g));
                    break;
                case 5:
                    System.out.println("Add friends edges person Vertex: " + addFriendsEdge(g));
                    break;
                case 6:
                    System.out.println("Get friends: " + getFriends(g));
                    break;
                case 7:
                    System.out.println("Find friends of friends: " + getFriendsOfFriends(g));
                    break;
                case 8:
                    System.out.println("Find path between peoples: " + findPathBetweenPeople(g));
                case 9:
                    System.out.println("Get persons Vertex limit: " + getPersonsLimit(g));
                    break;
                default:
                    System.out.println("Sorry, please enter valid Option");
            }
        }

        System.out.println("Exiting DiningByFriends, Bye!");
    }

    public static int showMenu() {

        int option = -1;
        Scanner keyboard = new Scanner(System.in);
        System.out.println();
        System.out.println("Main Menu:");
        System.out.println("--------------");
        System.out.println("1) Get person Vertex");
        System.out.println("2) Add person Vertex");
        System.out.println("3) Update person Vertex");
        System.out.println("4) Delete person Vertex");
        System.out.println("5) Add friends Edge");
        System.out.println("6) Find your Friends");
        System.out.println("7) Find the Friends of your Friends");
        System.out.println("8) Find the path between two people");
        System.out.println("9) Get persons with limit");
        System.out.println("0) Quit");
        System.out.println("--------------");
        System.out.println("Enter your choice:");
        option = keyboard.nextInt();

        return option;
    }

    public static Cluster connectToDatabase() {
        return Cluster.build()
                .port(8182)
                .addContactPoint("localhost")
                .credentials("root", "rootroot")
                .create();
    }

    public static GraphTraversalSource getGraphTraversalSource(Cluster cluster) {
        return traversal().withRemote(DriverRemoteConnection.using(cluster));
    }

    public static String getPerson(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the first name for the person to find:");
        String name = keyboard.nextLine();

        var properties = g.V().has("person", "first_name", name)
                .valueMap("first_name")
                .toList();
        return properties.toString();
    }

    public static String addPerson(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to add:");
        String name = keyboard.nextLine();

        var vertex = g.addV("person")
                .property("first_name", name)
                .next();

        return vertex.toString();
    }

    public static String getPersonsLimit(GraphTraversalSource g) {
        System.out.println("Enter the number of persons to return:");
        var keyboard = new Scanner(System.in);
        var limit = keyboard.nextInt();

        System.out.println("Returning " + limit + " persons:");
        var persons = g.V().hasLabel("person")
                .values("first_name")
                .limit(limit)
                .fold()
                .next();

        return persons.toString();
    }

    public static String updatePerson(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to update:");
        String name = keyboard.nextLine();
        System.out.println("Enter the new name for the person:");
        String newName = keyboard.nextLine();

        var vertex = g.V().has("person", "first_name", name)
                .property("first_name", newName)
                .next();
        return vertex.toString();
    }

    public static String deletePerson(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to delete:");
        String name = keyboard.nextLine();
        var result = g.V().has("person", "first_name", name)
                .sideEffect(drop())
                .count()
                .next();

        return String.valueOf(result);
    }

    public static String addFriendsEdge(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to start the edge at:");
        String fromName = keyboard.nextLine();
        System.out.println("Enter the name for the person to end the edge at:");
        String toName = keyboard.nextLine();

        var newEdge = g.addE("friends")
                .from(V().has("person", "first_name", fromName))
                .to(V().has("person", "first_name", toName))
                .next();

        return newEdge.toString();
    }

    public static String getFriends(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to find the friends of:");
        String name = keyboard.nextLine();

        var result = g.V().has("person", "first_name", name)
                .out("friends")
                .values("first_name")
                .toList();

        return result.toString();
    }

    public static String getFriendsOfFriends(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to find the friends of:");
        String name = keyboard.nextLine();

        var result = g.V().has("person", "first_name", name)
                .repeat(out("friends"))
                .times(2)
                .dedup()
                .values("first_name")
                .toList();

        return result.toString();
    }

    public static String findPathBetweenPeople(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the name for the person to start the edge at:");
        String fromName = keyboard.nextLine();
        System.out.println("Enter the name for the person to end the edge at:");
        String toName = keyboard.nextLine();

        var result = g.V().has("person", "first_name", fromName)
                .until(has("person", "first_name", toName))
                .repeat(both("friends").simplePath())
                .path()
                .toList();

        return result.toString();
    }
}
