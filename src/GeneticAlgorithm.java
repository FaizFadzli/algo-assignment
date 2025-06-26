import java.util.*;

public class GeneticAlgorithm {
    public List<Solution> population;
    private int populationSize;
    private int maxGenerations;
    private double crossoverRate;
    private double mutationRate;

    public GeneticAlgorithm(int populationSize, int maxGenerations, double crossoverRate, double mutationRate) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.population = new ArrayList<>();
    }

    // Initialize population with random rider assignments
    public void initializePopulation(List<Integer> allOrders, int numRiders) {
        Random rand = new Random();

        for (int i = 0; i < populationSize; i++) {
            List<Integer> shuffledOrders = new ArrayList<>(allOrders);
            Collections.shuffle(shuffledOrders);

            List<Chromosome> routes = new ArrayList<>();
            int index = 0;
            for (int r = 0; r < numRiders; r++) {
                List<Integer> riderOrders = new ArrayList<>();
                int limit = shuffledOrders.size() / numRiders;
                for (int j = 0; j < limit && index < shuffledOrders.size(); j++) {
                    riderOrders.add(shuffledOrders.get(index++));
                }
                routes.add(new Chromosome(riderOrders));
            }

            // Remaining orders go to the last rider
            while (index < shuffledOrders.size()) {
                routes.get(numRiders - 1).deliverySequence.add(shuffledOrders.get(index++));
            }

            population.add(new Solution(routes));
        }
    }

    // Evaluate total fitness for each solution
    public void evaluateFitness(List<DeliveryPoint> graph, int maxOrdersPerRider) {
        for (Solution s : population) {
            s.calculateTotalFitness(graph, maxOrdersPerRider);
        }
    }

    // Evolve population over generations
    public void evolveMulti(List<DeliveryPoint> graph, int maxOrdersPerRider) {
        for (int generation = 0; generation < maxGenerations; generation++) {
            evaluateFitness(graph, maxOrdersPerRider);
            List<Solution> newPopulation = new ArrayList<>();

            while (newPopulation.size() < populationSize) {
                Solution parent1 = selectParent();
                Solution parent2 = selectParent();

                Solution child;
                if (Math.random() < crossoverRate) {
                    child = crossoverMulti(parent1, parent2);
                } else {
                    child = cloneSolution(parent1);
                }

                if (Math.random() < mutationRate) {
                    child = mutateMulti(child);
                }

                child.calculateTotalFitness(graph, maxOrdersPerRider);
                newPopulation.add(child);
            }

            population = newPopulation;
            System.out.println("Generation " + generation + " | Best Fitness: " + getBestSolution().totalFitness);
        }
    }

    // Tournament selection
    public Solution selectParent() {
        List<Solution> tournament = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < 3; i++) {
            tournament.add(population.get(rand.nextInt(population.size())));
        }
        tournament.sort(Comparator.comparingDouble(s -> s.totalFitness));
        return cloneSolution(tournament.get(0));
    }

    // Simple crossover: pick each rider's route from either parent
    public Solution crossoverMulti(Solution p1, Solution p2) {
        Random rand = new Random();
        List<Chromosome> newRoutes = new ArrayList<>();

        for (int i = 0; i < p1.riderRoutes.size(); i++) {
            Chromosome selected = rand.nextBoolean() ? p1.riderRoutes.get(i) : p2.riderRoutes.get(i);
            newRoutes.add(new Chromosome(new ArrayList<>(selected.deliverySequence))); // copy
        }

        return new Solution(newRoutes);
    }

    // Mutate each rider route by swapping 2 locations (chance per rider)
    public Solution mutateMulti(Solution s) {
        Random rand = new Random();
        List<Chromosome> newRoutes = new ArrayList<>();

        for (Chromosome route : s.riderRoutes) {
            List<Integer> seq = new ArrayList<>(route.deliverySequence);
            if (seq.size() >= 2 && rand.nextDouble() < 0.5) {
                int i = rand.nextInt(seq.size());
                int j = rand.nextInt(seq.size());
                Collections.swap(seq, i, j);
            }
            newRoutes.add(new Chromosome(seq));
        }

        return new Solution(newRoutes);
    }

    // Best solution in current population
    public Solution getBestSolution() {
        return Collections.min(population, Comparator.comparingDouble(s -> s.totalFitness));
    }

    // Helper to clone a solution
    public Solution cloneSolution(Solution original) {
        List<Chromosome> newRoutes = new ArrayList<>();
        for (Chromosome c : original.riderRoutes) {
            newRoutes.add(new Chromosome(new ArrayList<>(c.deliverySequence)));
        }
        return new Solution(newRoutes);
    }
}
