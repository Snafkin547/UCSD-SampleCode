/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import util.GraphLoader;


/**
 * @author Harunobu Ishii.
 * 
 * For the warm up assignment, you must implement your Graph in a class
 * named CapGraph.  Here is the stub file.
 *
 */
public class CapGraph implements Graph {

	/* (non-Javadoc)
	 * @see graph.Graph#addVertex(int)
	 */
	private Map<Integer, ArrayList<Integer>> map; 
	private int numVertices;
	private int numEdges;
	private Stack<Integer> vertices;
	
	
	public CapGraph() {
		map= new HashMap<Integer, ArrayList<Integer>>(); 
		numVertices=0;
		numEdges=0;
        vertices = new Stack<Integer>();
	}

	@Override
	public void addVertex(int num) {
		// TODO Auto-generated method stub
		ArrayList<Integer> neighbors = new  ArrayList<Integer>();
		map.put(num, neighbors);
		vertices.push(num);
		numVertices++;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#addEdge(int, int)
	 */
	@Override
	public void addEdge(int from, int to) {
		// TODO Auto-generated method stub
		map.get(from).add(to);
		numEdges++;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getEgonet(int)
	 */
	public Stack<Integer> getVertices(){
		
		return vertices;
	}
	
    public void getEdges(){
	
    	for(int i=0; i<vertices.size();i++) {
    		int n = vertices.get(i);
        	System.out.println(n);
        	System.out.println(this.map.get(n));
    	}
	}
	
	public boolean hasVertex(int num) {
		Iterator<Entry<Integer, ArrayList<Integer>>> itr = map.entrySet().iterator();
		
		while(itr.hasNext()) {
			Map.Entry pair = (Map.Entry) itr.next();
			if(num == (int) pair.getKey()) return true;
		}
		return false;
	}
	
	@Override
	public Graph getEgonet(int center) {
		// TODO Auto-generated method stub
		//find neighbors and create list of them
		//in loop, list sub-neigbors of them
		//if sub-neighbors are one of direct neighbors, put them in a Egonet list
		
		CapGraph Egonet = new CapGraph();
		Iterator<Entry<Integer, ArrayList<Integer>>> it = map.entrySet().iterator();
		
		//flag if neighbors exist
		boolean found=false;
		
		ArrayList<Integer> neighbors = new ArrayList<Integer>();
		ArrayList<Integer> subneighbors = new ArrayList<Integer>();


        //loop inside the map to find neighbors of the centre
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();			
			if(center==(int)pair.getKey()) {
				//Egonet.addVertex(center);
				Egonet.addVertex(center);
				neighbors = (ArrayList<Integer>) pair.getValue();
				for(int n : neighbors) {
					Egonet.addEdge(center, n);
				}
				found = true;
			}
		}
		
		// if neighbors exist, find intra-neighbors' links
		if(found) {
			Iterator<Entry<Integer, ArrayList<Integer>>> itr = map.entrySet().iterator();
			
			while(itr.hasNext()) {
				Map.Entry pair = (Map.Entry) itr.next();
				for(int n: neighbors) {
					if(n == (int) pair.getKey()) {

						Egonet.addVertex(n);
						Egonet.addEdge(n, center);
						
						subneighbors = (ArrayList<Integer>) pair.getValue();

						for(int sn:subneighbors) {
							if(neighbors.contains(sn)) Egonet.addEdge(n,sn);
						}
		        	}
			    }
		    }
		}
		return Egonet;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getSCCs()
	 */
	@Override
	public List<Graph> getSCCs() {
		// TODO Auto-generated method stub

		Stack<Integer> finished = DFS(this, vertices);

		CapGraph Gt = getTranspose();

		List<Graph> SCC = sccDFS(Gt, finished);

		return SCC;

	}
	
	public List<Graph> sccDFS(CapGraph G, Stack<Integer> vertices) {
		Set<Integer> visited = new HashSet<Integer>();
		List<Graph> sccList = new ArrayList<Graph>();
		
		while(!vertices.isEmpty()) {
			int v = vertices.pop();
			if(!visited.contains(v)) {
				CapGraph scc = new CapGraph();
				sccDFS_VISITED(G, v, visited, scc);
				sccList.add(scc);
			}
		}
		return sccList;
	}
	
	public void sccDFS_VISITED(CapGraph G, int v, Set<Integer> visited, CapGraph scc) {
		visited.add(v);
		
		for(int n : getVertexFrom(G, v)) {
			if(!visited.contains(n)) sccDFS_VISITED(G, n, visited, scc);
		}
		scc.addVertex(v);
	}
	
	public CapGraph getTranspose()  {
	    CapGraph transpose = new CapGraph();
	    
		Iterator<Entry<Integer, ArrayList<Integer>>> itt = map.entrySet().iterator();
		//iterate on this
		while(itt.hasNext()){
			Map.Entry pair = (Map.Entry) itt.next();
			int node = (int)pair.getKey();
			ArrayList<Integer> neighbors  = new ArrayList<Integer>();
			neighbors = (ArrayList<Integer>) pair.getValue();
			
			if(!transpose.hasVertex(node)) addVertex(node);

			for(int n : neighbors) {
				if(!transpose.hasVertex(n)) transpose.addVertex(n);
				transpose.addEdge(n,node);
    		}
	    }
		return transpose;
	}
	
	public Stack<Integer> DFS(CapGraph G, Stack<Integer> vertices) {
		HashSet<Integer> visited = new HashSet<Integer>();
		Stack<Integer> finished = new Stack<Integer>();
		
		while(!vertices.isEmpty()) {
			int v = vertices.pop();
			if(!visited.contains(v)) DFS_VISITED(G, v, visited, finished);
		}
		return finished;
	}
	
	public void DFS_VISITED(CapGraph G, int v, Set<Integer> visited, Stack<Integer> finished) {
		visited.add(v);
		
		for(int n : getNeighbors(v)) {
			if(!visited.contains(n)) DFS_VISITED(G, n, visited, finished);
		}
		finished.push(v);
	}
	
	public ArrayList<Integer> getNeighbors(int center) {
		//find neighbors and create list of them
			
		Iterator<Entry<Integer, ArrayList<Integer>>> it = map.entrySet().iterator();
	
		//empty list for neighbors
		ArrayList<Integer> neighbors = new ArrayList<Integer>();

        //loop inside the map to find neighbors of the centre
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();			
			if(center==(int)pair.getKey()) {
				neighbors = (ArrayList<Integer>) pair.getValue();
			}
		}

	return neighbors;
	}
	
	public ArrayList<Integer> getVertexFrom(CapGraph G, int vertice) {
		//find neighbors and create list of them
			
		Iterator<Entry<Integer, ArrayList<Integer>>> itn = G.map.entrySet().iterator();
		
		
		//empty list for neighbors
		ArrayList<Integer> vertexFrom = new ArrayList<Integer>();
		ArrayList<Integer> ans = new ArrayList<Integer>();
		
        //loop inside the map to find neighbors of the centre
		while(itn.hasNext()) {
			Map.Entry pair = (Map.Entry) itn.next();			
			if(vertice==(int)pair.getKey()) {
				vertexFrom = (ArrayList<Integer>) pair.getValue();
			}
		}


	return vertexFrom;
	}
	/* (non-Javadoc)
	 * @see graph.Graph#exportGraph()
	 */
	
	private static void findBiggestSCC(CapGraph g, List<Set<Integer>> sccs){
		
        //List sccGraphs
      		List<Graph> graphSCCs=g.getSCCs();
              
        //Add SCC members to the list sccs
        for(Graph graph : graphSCCs) {
            HashMap<Integer, HashSet<Integer>> curr = graph.exportGraph();
            TreeSet<Integer> scc = new TreeSet<Integer>();
            for (Map.Entry<Integer, HashSet<Integer>> entry : curr.entrySet()) {
                scc.add(entry.getKey());
            }
            sccs.add(scc);
        }
		
		int max =0;
		List<Set<Integer>> ans = new LinkedList<Set<Integer>>();
		for(Set<Integer> scc :sccs) {
			if(scc.size() > max) {
				ans.clear();
				max =scc.size();
				ans.add(scc);
			}
			else if(scc.size() == max) ans.add(scc);
		}
		if(ans.size()==0) System.out.print("There is no community in this graph" + "\n");
		if(ans.size()==1) System.out.print("This is the biggest SCC community with " + ans.get(0).size() + "\n" + "Vertices: " + ans.get(0) + "\n");
		if(ans.size()>1) {
			System.out.print("Following are the biggest SCC communities with " + ans.get(0).size() + "\n" + " vertices:" + "\n");
			for(Set<Integer> a : ans) {
				System.out.println(a + "\n");
			}
		}
	}
	
	
	public List<Integer> getGlobalMDS() {
		
		List<Integer> ans = new ArrayList<Integer>();
		
		//System.out.print(vertices.size());
		
		for(int count=1;  count<vertices.size(); count++) {
			List<Integer> MDS = new ArrayList<Integer>();
   		  ans = help(0, count, MDS);
   		  if(!ans.isEmpty()) break;
		}
	 return ans;
	}
	
	public List<Integer> help(int start, int count, List<Integer> MDS) {
		
		boolean flag=false;
		List<Integer> ans = new ArrayList<Integer>();
		
		if(count==1){
			for(int i=start; i<vertices.size(); i++) {
				List<Integer> temp = new ArrayList<Integer>();
				temp.add(vertices.get(i));
				//System.out.println(vertices.get(i));
				for(int n: getNeighbors(vertices.get(i))) temp.add(n);
				//System.out.print(MDS);
			    //System.out.println(temp);

			    for(int v:vertices) {
					if(MDS.contains(v)||temp.contains(v)) flag=true;
					else {
						flag=false;
						break;
					}
				}
				if(flag) { 
			        ans.add(vertices.get(i));
					break;
				}
    		}
		}
		
		else {
			for(int i=start; i<vertices.size(); i++) {
				List<Integer> temp= new ArrayList<Integer>();
				
				for(int m: MDS) temp.add(m);
				MDS.add(vertices.get(i));
				
				for(int n: getNeighbors(vertices.get(i))) MDS.add(n);

				ans = help(i+1, count-1, MDS);
				
				if(ans.isEmpty()) MDS = temp;
				
				if(!ans.isEmpty()){
			    	ans.add(vertices.get(i));
			    	break;
			    }
    		}
		}
		
	return ans;
	}
	
public List<Integer> greedyMDS() {
		
		List<Integer> mds = new ArrayList<Integer>();
		Set<Integer> covered = new HashSet<Integer>();
		Set<Integer> nodes = new HashSet<Integer>();
		int maxVertices =0;

		for(int v: vertices) nodes.add(v);
		
		while(!nodes.isEmpty()) {
			int maxCount=0;
			for(int v: nodes) {
				if(!covered.contains(v)) {
					int tempCount=0;
					for(int t: getNeighbors(v)) {
						if(!covered.contains(t)) tempCount++;	
					}
					if(tempCount>=maxCount) {
							maxVertices=v;
							maxCount=tempCount;
				    }
			    }
		     }
			covered.add(maxVertices);
			for(int n:getNeighbors(maxVertices)) {
				covered.add(n);
				nodes.remove(n);
			}
			mds.add(maxVertices);
			nodes.remove(maxVertices);
		}
	    return mds;
	}
	
	
	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
		// TODO Auto-generated method stub

	    /* Return the graph's connections in a readable format. 
	     * The keys in this HashMap are the vertices in the graph.
	     * The values are the nodes that are reachable via a directed
	     * edge from the corresponding key. 
		 * The returned representation ignores edge weights and 
		 * multi-edges.  */
		
		
		Iterator<Entry<Integer, ArrayList<Integer>>> ite = map.entrySet().iterator();
		HashMap<Integer, HashSet<Integer>> mainGraph = new HashMap<Integer, HashSet<Integer>>();
		
        //loop inside the map to find neighbors of the centre
		while(ite.hasNext()) {
			Map.Entry pair = (Map.Entry) ite.next();		
			int vertex = (int) pair.getKey();
			ArrayList<Integer> edges = (ArrayList<Integer>) pair.getValue();
			HashSet<Integer> neighbors = new HashSet<Integer>(edges);
			
			mainGraph.put(vertex,  neighbors);
		}
		
    	return mainGraph;
		
	}
    public static void main(String[] args) {
		
		//Load data from files
    	
    	//String filename="data/facebook_Capstone.txt";
		String filename="data/scc/test_6.txt";
		CapGraph g = new CapGraph(); 
		GraphLoader.loadGraph(g, filename);
			
		
		////<MDS Algorithm>////
		long startTime2 = System.nanoTime();
		System.out.println(g.greedyMDS());
		long endTime2 = System.nanoTime();
		double time2 = (endTime2 - startTime2)/1000000000.0;
		System.out.println("Time used for MDS: " + time2);
		
	    ////<GlobalMDS Algorithm>////
		long startTime1 = System.nanoTime();
		System.out.println(g.getGlobalMDS());
		long endTime1 = System.nanoTime();
		double time1 = (endTime1 - startTime1)/1000000000.0;
		System.out.println("Time used for GlobalMDS: " + time1);
			
		////<SCC Algorithm>////
		long startTime = System.nanoTime();
		//Empty List for each SCC networks
        List<Set<Integer>> sccs = new ArrayList<Set<Integer>>();
        //Visualize the biggest scc network in sccs
        findBiggestSCC(g, sccs);
        long endTime = System.nanoTime();
		double time = (endTime - startTime)/1000000000.0;
		System.out.println("Time used for SCC: " + time);

	
	}
	
}
