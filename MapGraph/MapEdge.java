package roadgraph;

public class MapEdge {
	MapNode   from;
	MapNode  to;
	String street;
	String roadType;
	double weight;
	
	
	public MapEdge(MapNode from, MapNode to, String street, String roadType, double distance) {

		this.from=from;
		this.to=to;
		this.street=street;
		this.roadType=roadType;
		this.weight=distance;
	}
	
	public MapNode getNeighborsOf(MapNode node)
	{
		if (node.equals(from)) 
			return to;
		else if (node.equals(to))
			return from;
		throw new IllegalArgumentException("Looking for " + "a point that is not in the edge");
	}
	
	public double getWeight()
	{
		return weight;
	}
	
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public MapNode getOtherNode(MapNode node)
	{
		if (node.equals(from)) 
			return to;
		else if (node.equals(to))
			return from;
		throw new IllegalArgumentException("Looking for " + "a point that is not in the edge");
	}
	
}
