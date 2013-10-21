
public class StringBuildingVisitor implements MarkovNodeVisitor<String> {

	private StringBuilder str = new StringBuilder("");
	
	//Append the last character of the current node's history to the string built so far
	@Override
	public void visit(MarkovNode<String> node) {
		String label = node.label;
		if(label.length() > 0)
		{
			str.append(label.charAt(label.length() - 1));
		}
	}
	
	@Override
	public String toString()
	{
		return str.toString().substring(0, str.length() - 1);
	}
}
