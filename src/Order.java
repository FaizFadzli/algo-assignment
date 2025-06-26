public class Order {
    public int id;
    public int deliveryPointId;
    public int timeWindowStart; // in minutes (e.g., 0 = now, 30 = in 30 mins)
    public int timeWindowEnd;

    public Order(int id, int deliveryPointId, int start, int end) {
        this.id = id;
        this.deliveryPointId = deliveryPointId;
        this.timeWindowStart = start;
        this.timeWindowEnd = end;
    }
}
