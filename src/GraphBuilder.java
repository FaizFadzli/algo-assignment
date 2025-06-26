import java.util.*;

public class GraphBuilder {
    public static List<DeliveryPoint> buildGraph() {
        List<DeliveryPoint> graph = new ArrayList<>();

        graph.add(new DeliveryPoint(0, "Rider Hub", 0, 0));
        graph.add(new DeliveryPoint(1, "Customer A", 1, 2));
        graph.add(new DeliveryPoint(2, "Customer B", 3, 1));
        graph.add(new DeliveryPoint(3, "Customer C", 4, 3));
        graph.add(new DeliveryPoint(4, "Customer D", 2, 4));
        graph.add(new DeliveryPoint(5, "Customer E", 5, 2));
        graph.add(new DeliveryPoint(6, "Customer F", 6, 3));
        graph.add(new DeliveryPoint(7, "Customer G", 7, 1));
        graph.add(new DeliveryPoint(8, "Customer H", 6, 5));

        connect(graph, 0, 1, 2.0);
        connect(graph, 0, 2, 2.5);
        connect(graph, 0, 3, 3.0);
        connect(graph, 0, 4, 3.5);
        connect(graph, 1, 2, 1.0);
        connect(graph, 1, 5, 2.0);
        connect(graph, 2, 3, 1.2);
        connect(graph, 3, 4, 1.3);
        connect(graph, 4, 5, 2.0);
        connect(graph, 5, 6, 1.5);
        connect(graph, 6, 7, 1.1);
        connect(graph, 7, 8, 1.3);
        connect(graph, 3, 8, 2.7);
        connect(graph, 0, 5, 4.0);
        connect(graph, 0, 6, 5.0);
        connect(graph, 0, 7, 6.0);
        connect(graph, 0, 8, 6.5);


        return graph;
    }

    private static void connect(List<DeliveryPoint> g, int from, int to, double cost) {
        g.get(from).addConnection(to, cost);
        g.get(to).addConnection(from, cost);
    }
}
