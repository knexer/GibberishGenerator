import java.util.HashMap;
import java.util.Random;


public class MarkovNode<T> {
	public ProbabilityDistribution<T> transitionProbabilities;
	public HashMap<T, MarkovNode<T>> transitions;
	public T label;
	
	public MarkovNode(ProbabilityDistribution<T> initialDistribution, T label) {
		transitionProbabilities = initialDistribution;
		transitions = new HashMap<T, MarkovNode<T>>();
		this.label = label;
	}

	//NOTE: watch the stack size.  Might have to make this nonrecursive or something lame like that
	public void visit(MarkovNodeVisitor<T> visitor, Random r)
	{
		visitor.visit(this);
		
		//if this is a terminal state, aka there are no outward transitions defined, we're done
		if(transitions.size() == 0) return;
		
		T nextLabel = transitionProbabilities.sample(r);
		MarkovNode<T> nextNode = transitions.get(nextLabel);
		
		//recurse on the selected next node
		nextNode.visit(visitor, r);
	}
}
