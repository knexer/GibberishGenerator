import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;


public class CountedOccurrencesDistribution<T> implements ProbabilityDistribution<T> {
	
	//should definitely store an integer count for each element in the support
	//so req'd ops:
		//look up an el't to increment
			//hash table for constant time lookup
			//binary search tree if T is comparable.  Lame requirement
		//sample (select an integer s, have some ordering on the occurrences, select the sth occurrence)
			//I'd like binary search, but if the support set is small then iteration through a linear table might be good enough
	//binary search tree with counts for each subtree?  Seems pretty efficient to me :D But it requires that T be self-comparable
	//hash table with linear scan for sampling?  Quicker increment, slower sampling, doesn't require self-comparability
	//I like the first sol'n better but the second one is the right choice :(
	
	private HashMap<T, BigInteger> counts;
	private BigInteger numOccurrences;
	
	public CountedOccurrencesDistribution()
	{
		counts = new HashMap<T, BigInteger>();
		numOccurrences = BigInteger.ZERO;
	}
	
	public void addOccurrence(T element)
	{
		addOccurrences(element, BigInteger.ONE);
	}
	
	public void addOccurrences(T element, BigInteger count)
	{
		if(count.compareTo(BigInteger.ZERO) <= 0) throw new IllegalArgumentException(count + " is not a valid number of occurrences!");
		if(!counts.containsKey(element))
		{
			counts.put(element, BigInteger.ZERO);
		}
		
		counts.put(element, counts.get(element).add(count));
		numOccurrences = numOccurrences.add(count);
	}
	
	@Override
	public T sample(Random r) {
		//no distribution is defined if no data has been seen yet
		if(numOccurrences.equals(BigInteger.ZERO)) throw new IllegalStateException();
		
		//find the toSelect'th occurrence in the table
		//the order is irrelevant so long as it isn't correlated with the Random object's output
		BigInteger toSelect = randomBigInt(r, numOccurrences);
		for(T elt : counts.keySet())
		{
			toSelect = toSelect.subtract(counts.get(elt));
			if(toSelect.compareTo(BigInteger.ZERO) < 0)
			{
				return elt;
			}
		}
		
		//this should be impossible.
		return null;
	}
	
	private BigInteger randomBigInt(Random r, BigInteger maximum)
	{
		BigInteger ret;
		do
		{
			ret = new BigInteger(maximum.bitLength(), r);
		}while(ret.compareTo(maximum) >= 0);
		
		return ret;
	}

}
