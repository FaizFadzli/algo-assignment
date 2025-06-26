import java.util.ArrayList;
import java.util.List;

public class Rider {
    public int id;
    public int maxCapacity;
    public List<Order> assignedOrders;

    public Rider(int id, int maxCapacity) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.assignedOrders = new ArrayList<>();
    }

    public boolean canTakeOrder() {
        return assignedOrders.size() < maxCapacity;
    }

    public void assignOrder(Order order) {
        if (canTakeOrder()) {
            assignedOrders.add(order);
        }
    }
}
