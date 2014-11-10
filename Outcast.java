public class Outcast {
	private WordNet wordnet;
	
	// constructor takes a WordNet object 
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}
	
	//given an array of WordNet nouns,return an outcast 
	public String outcast(String[]nouns) {
		int max = 0;
		String s = null;
		// what if A == B?
		for (String nounA : nouns) {
			int dist = 0;
			for (String nounB : nouns) {
				dist += wordnet.distance(nounA, nounB);
				if (dist > max) {
					max = dist;
					s = nounA;
				}
			}
		}
		return s;
	}
	
	// test client
	public static void main(String[]args) {
		
	}
}
