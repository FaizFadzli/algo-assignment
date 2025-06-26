import java.util.*;

public class Chromosome {
    public List<Integer> deliverySequence;
    public double fitness;

    public Chromosome() {
        this.deliverySequence = new ArrayList<>();
        this.fitness = Double.MAX_VALUE;
    }

    public Chromosome(List<Integer> sequence) {
        this.deliverySequence = new ArrayList<>(sequence);
        this.fitness = Double.MAX_VALUE;
    }

    public void calculateFitness(List<DeliveryPoint> graph) {
        double totalCost = 0.0;
        double penalty = 1000.0;

        for (int i = 0; i < deliverySequence.size() - 1; i++) {
            int fromId = deliverySequence.get(i);
            int toId = deliverySequence.get(i + 1);

            DeliveryPoint from = findPointById(graph, fromId);
            if (from != null && from.connections.containsKey(toId)) {
                totalCost += from.connections.get(toId);
            } else {
                totalCost += penalty;
            }
        }

        this.fitness = totalCost;
    }

    private DeliveryPoint findPointById(List<DeliveryPoint> graph, int id) {
        for (DeliveryPoint dp : graph) {
            if (dp.id == id) return dp;
        }
        return null;
    }
}
