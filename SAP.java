import java.util.*;

/* class used to calculate the Shortest Ancestral Path (SAP) */
public class SAP {
	private Digraph G;
	private int min;
	private int anc;
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		// make a deep, defensive copy of G
		this.G = new Digraph(G);
	}
	
	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		Set<Integer> vs = new HashSet<Integer>();
		vs.add(v);
		Set<Integer> ws = new HashSet<Integer>();
		ws.add(w);
		length(vs, ws);
		if (anc == -1) return -1;
		else return min;
	}
	
	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		length(v, w);
		return anc;
	}
	
	//length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		min = Integer.MAX_VALUE; 
		anc = -1;
		BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(G, w);
		for (int u = 0; u < G.V(); u++) {
			if (bfs1.hasPathTo(u) && bfs2.hasPathTo(u)) {
				int len = bfs1.distTo(u) + bfs2.distTo(u);
				if (len < min) {
					min = len;
					anc = u;
				}
			}
		}
		if (anc == -1) return -1; // not found the path
		else return min;
	}
	
	//a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		length(v, w);
		return anc;
	}
	
	//do unit testing of this class
	public static void main(String[] args) {
		In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		while (!StdIn.isEmpty()) {
			int v = StdIn.readInt();
			int w = StdIn.readInt(); 
			int length =sap.length(v,w); 
			int ancestor = sap.ancestor(v, w);
			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		}
	}
}