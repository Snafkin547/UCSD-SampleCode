package roadgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import geography.GeographicPoint;


public class MapNode implements Comparable<MapNode>{
	
	private GeographicPoint location;
	private List<MapEdge> edges;
	private LinkedList<MapNode> neighbors;
	private double distance;
	
	public MapNode(GeographicPoint location) {

		this.location = location;
     	edges = new ArrayList<>();
     	neighbors = new LinkedList<MapNode>();
     	distance = Double.MAX_VALUE;
	}
	
	public void  addEdge(MapEdge edge) {
		edges.add(edge);
	}
	
	public GeographicPoint getLocation() {
		
		return location;
	}
	
	/** Returns whether two nodes are equal.
	 * Nodes are considered equal if their locations are the same, 
	 * even if their street list is different.
	 * @param o the node to compare to
	 * @return true if these nodes are at the same location, false otherwise
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof MapNode) || (o == null)) {
			return false;
		}
		MapNode node = (MapNode)o;
		return node.location.equals(this.location);
	}
	
	public void addNeighbor(MapNode neighbor) 
	{
		neighbors.add(neighbor);
	}
	
	/**
	 * @return the neighbors
	 */
	public Set<MapNode> getNeighbors() {
		Set<MapNode> neighbors = new HashSet<MapNode>();
		for(MapEdge edge : edges) {
			neighbors.add(edge.getNeighborsOf(this));
		}
		return neighbors;
	}
	
    @SuppressWarnings("null")
	public double getEdge(MapNode start,MapNode end){
        Double edgeWeight = null;
        for(MapEdge edge: edges){
            if(edge.getOtherNode(start).equals(end)){
                edgeWeight =  edge.getWeight();
                return edgeWeight;
            }
        }
        return edgeWeight;
    }
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
    public int compareTo(MapNode o) {
        if(this.distance < o.distance){
            return -1;
        }else if(this.distance > o.distance){
            return 1;
        }else{
            return 0;    
        }
    }
}
