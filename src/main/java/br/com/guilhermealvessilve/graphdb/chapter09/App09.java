package br.com.guilhermealvessilve.graphdb.chapter09;

import org.apache.tinkerpop.gremlin.process.traversal.IO;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.step.util.WithOptions;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.util.Arrays;
import java.util.Scanner;

import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;
import static org.apache.tinkerpop.gremlin.process.traversal.P.within;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.V;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.both;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.drop;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.has;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.identity;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.in;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.inE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.outE;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.select;
import static org.apache.tinkerpop.gremlin.structure.Column.keys;
import static org.apache.tinkerpop.gremlin.structure.Column.values;

public class App09 {

    public static void main(String[] args) {
        try (var graph = TinkerGraph.open()) {
            var g = getGraphTraversalSource(graph);
            displayMenu(g);
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
                case 10:
                    System.out.println("Newest restaurant reviews: " + newestRestaurantReviews(g));
                    break;
                case 11:
                    System.out.println("Highest rated restaurants near me: " + highestRatedRestaurants(g));
                    break;
                case 12:
                    System.out.println("Highest rated restaurants by cuisine near me: " + highestRatedByCuisine(g));
                    break;
                case 13:
                    System.out.println("List of cuisine names: " + getCuisinesList(g));
                    break;
                case 14:
                    System.out.println("List Top 3 Friends Restaurants for City: " + findTop3FriendsRestaurantsForCity(g));
                    break;
                case 15:
                    System.out.println("List cities names: " + getCitiesList(g));
                    break;
                default:
                    System.out.println("Sorry, please enter valid Option");
            }
        }

        System.out.println("Exiting DiningByFriends, Bye!");
    }

    public static int showMenu() {
        int option;
        var keyboard = new Scanner(System.in);
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
        System.out.println("10) Find the newest reviews for a restaurant");
        System.out.println("11) What are the ten highest rated restaurants near me");
        System.out.println("12) What restaurant near me, with a specific cuisine, is the highest rated");
        System.out.println("13) List all cuisines");
        System.out.println("14) List Top 3 Friends restaurants for city");
        System.out.println("15) List all cities");
        System.out.println("0) Quit");
        System.out.println("--------------");
        System.out.println("Enter your choice:");
        option = keyboard.nextInt();
        return option;
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
                .valueMap("person_id", "first_name")
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

    private static String newestRestaurantReviews(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the id of the restaurant:");
        var id = keyboard.nextLong();
        var result = g.V().has("restaurant", "restaurant_id", id)
                .in("about")
                .order()
                .by("created_date", desc)
                .limit(3)
                .valueMap("rating", "created_date", "body")
                .with(WithOptions.tokens)
                .toList();

        return result.toString();
    }

    private static String highestRatedRestaurants(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the id of the person:");
        var id = keyboard.nextLong();
        var result = g.V().has("person", "person_id", id)
                .out("lives")
                .in("within")
                .where(inE("about"))
                .group()
                    .by(identity())
                    .by(in("about")
                            .values("rating")
                            .mean())
                .unfold()
                .order()
                    .by(values, desc)
                .limit(10)
                .project("name", "address", "rating_average")
                    .by(select(keys).values("name"))
                    .by(select(keys).values("address"))
                    .by(select(values))
                .toList();

        return result.toString();
    }

    private static String highestRatedByCuisine(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the id for the person:");
        var id = Integer.valueOf(keyboard.nextLine());
        System.out.println("Enter a comma separated list of cuisines:");
        var cuisineList = Arrays.asList(keyboard.nextLine().split(","));
        cuisineList.replaceAll(String::trim);

        var result = g.V().has("person", "person_id", id)
                .out("lives")
                .in("within")
                .where(out("serves").has("name", within(cuisineList)))
                .where(inE("about"))
                .group()
                .by(identity())
                .by(in("about")
                        .values("rating")
                        .mean())
                .unfold()
                .order()
                .by(values, desc)
                .limit(10)
                .project("name", "address", "cuisine")
                .by(select(keys).values("name"))
                .by(select(keys).values("address"))
                .by(select(keys).out("serves").values("name"))
                .toList();

        return result.toString();
    }

    private static String getCuisinesList(GraphTraversalSource g) {
        var result = g.V().hasLabel("cuisine")
                .values("name")
                .dedup()
                .toList();
        return result.toString();
    }

    private static String findTop3FriendsRestaurantsForCity(GraphTraversalSource g) {
        var keyboard = new Scanner(System.in);
        System.out.println("Enter the first name for the person: ");
        String name = keyboard.nextLine();
        System.out.println("Enter the city to search in: ");
        String city = keyboard.nextLine();

        System.out.println("Graph: " + g);
        var sg = getSubgraphOfFriends(g, name);
        System.out.println("Subgraph: " + sg);
        var result = sg.V().has("city", "name", city)
                .in("within")
                .where(inE("about"))
                .group()
                .by(identity())
                .by(in("about")
                        .values("rating")
                        .mean())
                .unfold()
                .order()
                .by(values, desc)
                .limit(3)
                .project("restaurant_id", "restaurant_name", "address", "rating_average")
                .by(select(keys).values("restaurant_id"))
                .by(select(keys).values("restaurant_name"))
                .by(select(keys).values("address"))
                .by(select(values))
                .toList();
        return result.toString();
    }

    private static String getCitiesList(GraphTraversalSource g) {
        var result = g.V().hasLabel("city")
                .values("name")
                .dedup()
                .toList();
        return result.toString();
    }

    private static GraphTraversalSource getSubgraphOfFriends(GraphTraversalSource g,
                                                             String firstName) {
        var sg = (Graph) g.V().has("person", "first_name", firstName)
                .bothE("friends").subgraph("sg").otherV()
                .outE("wrote").subgraph("sg").inV()
                .optional(outE("about").subgraph("sg").inV())
                .outE("within").subgraph("sg")
                .cap("sg")
                .next();
        return sg.traversal();
    }
}
