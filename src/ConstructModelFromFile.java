import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;


public class ConstructModelFromFile {
	
	
	public static void main(String[] args)
	{
		String filename = "w.txt";
		int n = 3;
		int numWords = 20;
		
		String corpus = null;
		
		try{
			corpus = readFile(filename, StandardCharsets.UTF_8);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
		
		HashMap<String, BigInteger> corpusMap = constructCorpusFromString(corpus);
		NGramFactory factory = new NGramFactory(n, corpusMap);
		MarkovNode<String> model = factory.constructModel();
		model.toString();
		
		Random r = new Random();
		for(int i = 0; i < numWords; i++)
		{
			MarkovNodeVisitor<String> visitor = new StringBuildingVisitor();
			model.visit(visitor, r);
			
			String generatedWord = visitor.toString();
			
			//if word is in the dictionary, skip it
			if(corpusMap.containsKey(generatedWord) && corpusMap.get(generatedWord).compareTo(new BigInteger("1000")) > 0)
			{
				i--;
			}
			else
			{
				System.out.println(generatedWord);
			}
		}
	}
	
	private static HashMap<String, BigInteger> constructCorpusFromString(String corpus)
	{
		HashMap<String, BigInteger> ret = new HashMap<String, BigInteger>();
		
		//for each word in the corpus
		String[] lines = corpus.split("\n");
		for(String line : lines)
		{
			//extract the word and count from the formatted line
			int tabLocation = line.indexOf('\t');
			String word = line.substring(0, tabLocation);
			BigInteger count = new BigInteger(line.substring(tabLocation + 1));
			
			ret.put(word, count);
		}
		
		return ret;
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return encoding.decode(ByteBuffer.wrap(encoded)).toString();
			}
}
