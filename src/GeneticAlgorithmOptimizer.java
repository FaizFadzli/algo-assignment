import java.util.*;

public class GeneticAlgorithmOptimizer {

    public static class Result {
        public Map<Integer, List<Order>> routes;
        public double totalFitness;

        public Result(Map<Integer, List<Order>> routes, double fitness) {
            this.routes = routes;
            this.totalFitness = fitness;
        }
    }

    public static Result optimize(List<DeliveryPoint> graph, List<Order> orders, List<Rider> originalRiders, int maxGenerations) {
        List<Map<Integer, List<Order>>> population = new ArrayList<>();
        Random random = new Random();

        // Initialize population with random assignments
        for (int i = 0; i < 10; i++) {
            population.add(generateRandomAssignment(orders, originalRiders));
        }

        Map<Integer, List<Order>> bestIndividual = null;
        double bestFitness = Double.MAX_VALUE;

        for (int generation = 0; generation < maxGenerations; generation++) {
            for (Map<Integer, List<Order>> individual : population) {
                double fitness = calculateFitness(individual, graph);

                if (fitness < bestFitness) {
                    bestFitness = fitness;
                    bestIndividual = deepCopy(individual);
                }
            }

            // Simple mutation logic (optional: implement crossover)
            population = mutatePopulation(population, orders, originalRiders);
        }

        return new Result(bestIndividual, bestFitness);
    }

    private static Map<Integer, List<Order>> generateRandomAssignment(List<Order> orders, List<Rider> originalRiders) {
        Map<Integer, List<Order>> routes = new HashMap<>();
        Random random = new Random();

        // Reset riders
        List<Rider> riders = new ArrayList<>();
        for (Rider r : originalRiders) {
            riders.add(new Rider(r.id, r.maxCapacity));
        }

        for (Order order : orders) {
            Rider chosen = null;
            int attempts = 0;
            while (attempts < 10) {
                Rider candidate = riders.get(random.nextInt(riders.size()));
                if (candidate.canTakeOrder()) {
                    chosen = candidate;
                    break;
                }
                attempts++;
            }

            if (chosen != null) {
                chosen.assignOrder(order);
                routes.computeIfAbsent(chosen.id, k -> new ArrayList<>()).add(order);
            }
        }

        return routes;
    }

    private static double calculateFitness(Map<Integer, List<Order>> routes, List<DeliveryPoint> graph) {
        double total = 0;
        for (Map.Entry<Integer, List<Order>> entry : routes.entrySet()) {
            int fromId = 0; // start at hub
            for (Order order : entry.getValue()) {
                int toId = order.deliveryPointId;
                DeliveryPoint from = findPointById(graph, fromId);
                if (from != null && from.connections.containsKey(toId)) {
                    total += from.connections.get(toId);
                    fromId = toId;
                } else {
                    total += 9999; // unreachable penalty
                }
            }
        }
        return total;
    }

    private static DeliveryPoint findPointById(List<DeliveryPoint> graph, int id) {
        for (DeliveryPoint dp : graph) {
            if (dp.id == id) return dp;
        }
        return null;
    }

    private static Map<Integer, List<Order>> deepCopy(Map<Integer, List<Order>> original) {
        Map<Integer, List<Order>> copy = new HashMap<>();
        for (Map.Entry<Integer, List<Order>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    private static List<Map<Integer, List<Order>>> mutatePopulation(List<Map<Integer, List<Order>>> population, List<Order> orders, List<Rider> riders) {
        List<Map<Integer, List<Order>>> mutated = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            mutated.add(generateRandomAssignment(orders, riders));  // simple re-randomization
        }
        return mutated;
    }
}
