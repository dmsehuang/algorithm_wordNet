import java.util.*;

public class WordNet {
	private HashMap<String, ArrayList<Integer>> strIdMap;
	private ArrayList<String> strs;
	private Digraph G;
	private int ancestor;
	private int min;
	private SAP sap;
	
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new java.lang.NullPointerException();
		}
		
		// parse and construct the nouns
		strIdMap = new HashMap<String, ArrayList<Integer>>();
		strs = new ArrayList<String>();
		In in = new In(synsets);
		String line = null;
		while((line = in.readLine()) != null) {
			String[] parts = line.split(",");
			int id = Integer.parseInt(parts[0]);
			// construct id - synset map
			strs.add(parts[1]);
			// construct word - id's map
			String[] words = parts[1].split("\\s");
			for (String word : words) {
				if (!strIdMap.containsKey(word)) {
					strIdMap.put(word, new ArrayList<Integer>());
				}
				ArrayList<Integer> ids = strIdMap.get(word);
				ids.add(id);
				strIdMap.put(word, ids);
			}
		}
		
		// parse and construct the digraph
		G = new Digraph(strs.size());
		in = new In(hypernyms);
		while((line = in.readLine()) != null) {
			String[] ids = line.split(",");
			int id = Integer.parseInt(ids[0]);
			for (int i = 1; i < ids.length; i++) {
				G.addEdge(id, Integer.parseInt(ids[i]));
			}
		}
		
		// check if G has one and only one root
		int root = 0;
		for (int v = 0; v < G.V(); v++) {
			Iterator<Integer> iter = G.adj(v).iterator();
			if (!iter.hasNext()) {
				root++;
			}
		}
		if (root != 1) {
			throw new java.lang.IllegalArgumentException("must exactly has one root!");
		}
		
		// check if G is a DAG
		DirectedCycle dc = new DirectedCycle(G);
		if (dc.hasCycle()) {
			throw new java.lang.IllegalArgumentException("has cycle!");
		}
		
		// init
		sap = new SAP(G);
		ancestor = -1;
		min = Integer.MAX_VALUE;
	}
	
	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return strIdMap.keySet();
	}
	
	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		if (word == null) {
			throw new java.lang.NullPointerException();
		}
		if (strIdMap.keySet().contains(word)) {
			return true;
		} else {
			return false;
		}
	}
	
	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		sap(nounA, nounB);
		if (ancestor == -1) {
			throw new java.lang.IllegalArgumentException();
		} else {
			return min;
		}
	}
	
	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB 
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		if (nounA == null || nounB == null) {
			throw new java.lang.NullPointerException();
		}
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new java.lang.IllegalArgumentException("both of them should be nouns!");
		}
		
		min = Integer.MAX_VALUE;
		ancestor = -1;
		// check all possible synsets that contains A & B
		for (int v : strIdMap.get(nounA)) {
			for (int w : strIdMap.get(nounB)) {
				int len = sap.length(v, w);
				if (len < min) {
					min = len;
					ancestor = sap.ancestor(v, w);
				}
			}
		}
		if (ancestor == -1) {
			throw new java.lang.IllegalArgumentException();
		} else {
			return strs.get(ancestor);
		}
	}
	
	// do unit testing of this class
	public static void main(String[] args) {
		WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
	}
}
