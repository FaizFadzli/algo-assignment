import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {
    public List<Chromosome> riderRoutes;
    public double totalFitness;

    public Solution(List<Chromosome> routes) {
        this.riderRoutes = routes;
        this.totalFitness = Double.MAX_VALUE;
    }

    public void calculateTotalFitness(List<DeliveryPoint> graph, int maxOrdersPerRider) {
        double sum = 0;
        Set<Integer> assigned = new HashSet<>();

        for (Chromosome c : riderRoutes) {
            // Penalize duplicate customer assignments
            for (int id : c.deliverySequence) {
                if (assigned.contains(id)) {
                    sum += 500; // Penalty for duplicate delivery
                } else {
                    assigned.add(id);
                }
            }

            if (c.deliverySequence.size() > maxOrdersPerRider) {
                sum += (c.deliverySequence.size() - maxOrdersPerRider) * 100;
            }

            c.calculateFitness(graph);
            sum += c.fitness;
        }

        this.totalFitness = sum;
    }

}
