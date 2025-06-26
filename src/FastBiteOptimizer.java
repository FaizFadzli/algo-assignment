import java.util.*;

public class FastBiteOptimizer {
    public static void main(String[] args) {
        List<DeliveryPoint> graph = GraphBuilder.buildGraph();

        List<Integer> allOrders = new ArrayList<>();
        for (DeliveryPoint dp : graph) {
            if (dp.id != 0) allOrders.add(dp.id); // 0 is Rider Hub
        }

        // GA Parameters
        int populationSize = 50;
        int maxGenerations = 100;
        double crossoverRate = 0.8;
        double mutationRate = 0.3;
        int numRiders = 3;
        int maxOrdersPerRider = 4;

        // 🕒 Start timing
        long startTime = System.nanoTime();

        GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, maxGenerations, crossoverRate, mutationRate);
        ga.initializePopulation(allOrders, numRiders);
        ga.evolveMulti(graph, maxOrdersPerRider);
        Solution best = ga.getBestSolution();

        // 🕒 End timing
        long endTime = System.nanoTime();
        double durationMs = (endTime - startTime) / 1_000_000.0;

        // ✅ Output results
        System.out.println("\n📦 Genetic Algorithm – Best Multi-Rider Delivery Plan:");
        for (int i = 0; i < best.riderRoutes.size(); i++) {
            System.out.println("Rider " + (i + 1) + ": " + best.riderRoutes.get(i).deliverySequence);
        }
        System.out.printf("🧮 Total Delivery Cost (Fitness): %.2f\n", best.totalFitness);
        System.out.printf("🕒 GA Execution Time: %.2f ms\n", durationMs);
    }
}
