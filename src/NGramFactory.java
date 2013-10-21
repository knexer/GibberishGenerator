import java.math.BigInteger;
import java.util.HashMap;


public class NGramFactory {
	private int n;
	private HashMap<String, BigInteger> corpus;
	
	private HashMap<String, MarkovNode<String>> nodeSet;
	
	public NGramFactory(int n, HashMap<String, BigInteger> corpus)
	{
		this.n = n;
		this.corpus = corpus;
	}
	
	public MarkovNode<String> constructModel()
	{
		//initialize the model
		MarkovNode<String> ret = new MarkovNode<String>(new CountedOccurrencesDistribution<String>(), "");
		MarkovNode<String> terminus = new MarkovNode<String>(null, " ");
		
		nodeSet = new HashMap<String, MarkovNode<String>>();
		nodeSet.put("", ret);
		nodeSet.put(" ", terminus);
		
		//for each word in the corpus
		for(String word : corpus.keySet())
		{
			BigInteger count = corpus.get(word);
			
			MarkovNode<String> currentNode = ret;
			String history = "";
			
			//teach the model the word
			for(char c : word.toCharArray())
			{
				//update the history with this newly seen character
				history = history + c;
				if(history.length() > n)
				{
					history = history.substring(1);
				}
				
				addOccurrences(currentNode, history, count);
				
				//transition to the next node
				currentNode = currentNode.transitions.get(history);
			}
			
			//now currentNode's label is the last subsequence of length n in the word
			//(unless there is no such substring, in which case it's the whole word)
			//this node should have a transition to the terminal state
			addOccurrences(currentNode, " ", count);
		}
		
		return ret;
	}

	private void addOccurrences(MarkovNode<String> currentNode, String history, BigInteger count)
	{
		//update the probability distribution
		CountedOccurrencesDistribution<String> distr = (CountedOccurrencesDistribution<String>) currentNode.transitionProbabilities;
		distr.addOccurrences(history, count);
		
		//if the current node doesn't have a reference to the computed next node
		if(!currentNode.transitions.containsKey(history))
		{
			//if the node's been generated before
			if(nodeSet.containsKey(history))
			{
				//get a reference to it
				currentNode.transitions.put(history, nodeSet.get(history));
			}
			else
			{
				//otherwise generate it now
				MarkovNode<String> nextNode =  new MarkovNode<String>(new CountedOccurrencesDistribution<String>(), history);
				nodeSet.put(history, nextNode);
				currentNode.transitions.put(history, nextNode);
			}
		}
		
	}
}
