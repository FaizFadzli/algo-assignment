import java.util.*;

public class UnitTest {

    public static void main(String[] args) {
        testGeneticAlgorithm();
        testSortingBasedAssignment();
    }

    private static void testGeneticAlgorithm() {
        System.out.println("\n🧪 Running Unit Test: Genetic Algorithm");

        List<DeliveryPoint> graph = GraphBuilder.buildGraph();
        List<Order> orders = generateTestOrders();
        List<Rider> riders = generateTestRiders();

        GeneticAlgorithmOptimizer.Result result = GeneticAlgorithmOptimizer.optimize(graph, orders, riders, 10);

        System.out.println("➡️ Checking GA result object...");
        assert result != null : "❌ GA Result should not be null";
        System.out.println("✅ GA result object is not null");

        assert result.routes != null : "❌ GA Routes should not be null";
        System.out.println("✅ GA routes are not null");

        assert result.totalFitness >= 0 : "❌ GA Fitness should be non-negative";
        System.out.println("✅ GA fitness is non-negative: " + result.totalFitness);

        System.out.println("➡️ Checking for duplicate orders in GA...");
        checkNoDuplicates(result.routes);

        System.out.println("➡️ Checking capacity limits for riders in GA...");
        checkCapacityLimits(result.routes, 4);

        System.out.println("✅ All Genetic Algorithm tests passed!");
    }

    private static void testSortingBasedAssignment() {
        System.out.println("\n🧪 Running Unit Test: Sorting-Based Assignment");

        List<DeliveryPoint> graph = GraphBuilder.buildGraph();
        List<Order> orders = generateTestOrders();
        List<Rider> riders = generateTestRiders();

        SortingBasedAssignment.AssignmentResult result = SortingBasedAssignment.assignOrders(graph, orders, riders);

        System.out.println("➡️ Checking SBA result object...");
        assert result != null : "❌ SBA Result should not be null";
        System.out.println("✅ SBA result object is not null");

        assert result.assignedRoutes != null : "❌ SBA Routes should not be null";
        System.out.println("✅ SBA routes are not null");

        assert result.totalCost >= 0 : "❌ SBA Cost should be non-negative";
        System.out.println("✅ SBA cost is non-negative: " + result.totalCost);

        System.out.println("➡️ Checking for duplicate orders in SBA...");
        checkNoDuplicates(result.assignedRoutes);

        System.out.println("➡️ Checking capacity limits for riders in SBA...");
        checkCapacityLimits(result.assignedRoutes, 4);

        System.out.println("✅ All Sorting-Based Assignment tests passed!");
    }

    private static void checkNoDuplicates(Map<Integer, List<Order>> routes) {
        Set<Integer> seen = new HashSet<>();
        for (List<Order> orders : routes.values()) {
            for (Order o : orders) {
                assert !seen.contains(o.id) : "❌ Duplicate order found: " + o.id;
                seen.add(o.id);
            }
        }
        System.out.println("✅ No duplicate orders found.");
    }

    private static void checkCapacityLimits(Map<Integer, List<Order>> routes, int maxCapacity) {
        for (Map.Entry<Integer, List<Order>> entry : routes.entrySet()) {
            assert entry.getValue().size() <= maxCapacity :
                "❌ Rider " + entry.getKey() + " exceeded capacity.";
        }
        System.out.println("✅ All riders are within capacity limits.");
    }

    private static List<Order> generateTestOrders() {
        return new ArrayList<>(List.of(
            new Order(1, 1, 0, 30),
            new Order(2, 2, 0, 30),
            new Order(3, 3, 0, 30),
            new Order(4, 4, 0, 30),
            new Order(5, 5, 0, 30),
            new Order(6, 6, 0, 30),
            new Order(7, 7, 0, 30),
            new Order(8, 8, 0, 30)
        ));
    }

    private static List<Rider> generateTestRiders() {
        return new ArrayList<>(List.of(
            new Rider(1, 4),
            new Rider(2, 4),
            new Rider(3, 4)
        ));
    }
}
