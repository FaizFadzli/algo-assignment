import java.util.*;

public class DeliveryPoint {
    public int id;
    public String name;
    public double x, y;
    public Map<Integer, Double> connections;

    public DeliveryPoint(int id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.connections = new HashMap<>();
    }

    public void addConnection(int destinationId, double cost) {
        connections.put(destinationId, cost);
    }
}
