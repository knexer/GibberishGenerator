import java.util.Random;


public interface ProbabilityDistribution<T> {
	public T sample(Random r);
}
