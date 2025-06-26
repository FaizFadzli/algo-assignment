import java.util.*;

public class SortingBasedAssignment {

    private static final double UNREACHABLE_PENALTY = 9999;
    private static final double UNASSIGNED_ORDER_PENALTY = 1000;

    public static class AssignmentResult {
        public Map<Integer, List<Order>> assignedRoutes;
        public double totalCost;

        public AssignmentResult(Map<Integer, List<Order>> assignedRoutes, double totalCost) {
            this.assignedRoutes = assignedRoutes;
            this.totalCost = totalCost;
        }
    }

    public static AssignmentResult assignOrders(List<DeliveryPoint> graph, List<Order> orders, List<Rider> riders) {
        orders.sort(Comparator.comparingInt(o -> o.timeWindowStart));
        Map<Integer, List<Order>> assignedRoutes = new HashMap<>();
        double totalCost = 0;

        for (Order order : orders) {
            Rider bestRider = null;
            double minCost = Double.MAX_VALUE;

            List<Rider> shuffledRiders = new ArrayList<>(riders);
            Collections.shuffle(shuffledRiders);

            for (Rider rider : shuffledRiders) {
                if (rider.canTakeOrder()) {
                    int fromId = 0;
                    int toId = order.deliveryPointId;
                    DeliveryPoint from = findPointById(graph, fromId);
                    double cost = (from != null && from.connections.containsKey(toId)) ?
                            from.connections.get(toId) : UNREACHABLE_PENALTY;

                    if (cost < minCost) {
                        minCost = cost;
                        bestRider = rider;
                    }
                }
            }

            if (bestRider != null && minCost < UNREACHABLE_PENALTY) {
                bestRider.assignOrder(order);
                assignedRoutes.computeIfAbsent(bestRider.id, k -> new ArrayList<>()).add(order);
                totalCost += minCost;
            } else {
                totalCost += UNASSIGNED_ORDER_PENALTY;
            }
        }

        return new AssignmentResult(assignedRoutes, totalCost);
    }

    private static DeliveryPoint findPointById(List<DeliveryPoint> graph, int id) {
        for (DeliveryPoint dp : graph) {
            if (dp.id == id) return dp;
        }
        return null;
    }

    public static void main(String[] args) {
        List<DeliveryPoint> graph = GraphBuilder.buildGraph();

        List<Order> orders = new ArrayList<>(List.of(
            new Order(1, 1, 0, 30),
            new Order(2, 2, 0, 30),
            new Order(3, 3, 0, 30),
            new Order(4, 4, 0, 30),
            new Order(5, 5, 0, 30),
            new Order(6, 6, 0, 30),
            new Order(7, 7, 0, 30),
            new Order(8, 8, 0, 30)
        ));

        List<Rider> riders = new ArrayList<>(List.of(
            new Rider(1, 4),
            new Rider(2, 4),
            new Rider(3, 4)
        ));

        long start = System.nanoTime();
        AssignmentResult result = assignOrders(graph, orders, riders);
        long end = System.nanoTime();
        double duration = (end - start) / 1_000_000.0;

        System.out.println("\n📦 Sorting-Based Delivery Assignment (Enhanced):");
        for (Rider rider : riders) {
            List<Order> assigned = result.assignedRoutes.getOrDefault(rider.id, new ArrayList<>());
            System.out.print("Rider " + rider.id + ": ");
            for (Order o : assigned) {
                System.out.print("Order" + o.id + " ");
            }
            if (assigned.isEmpty()) {
                System.out.print("[No orders assigned]");
            }
            System.out.println();
        }

        System.out.printf("🧮 Total Delivery Cost (Enhanced Fitness): %.2f\n", result.totalCost);
        System.out.printf("🕒 SBA Execution Time: %.2f ms\n", duration);
    }
}
