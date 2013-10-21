import java.io.File;
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
	
	/**
	 * 
	 * @param args
	 * args[0] training data directory
	 * args[1] n (size of history, an integer)
	 * args[2] numWords (number of gibberish words to generate)
	 */
	public static void main(String[] args)
	{
		File inputDirectory = new File(args[0]);
		int n = Integer.parseInt(args[1]);
		int numWords = Integer.parseInt(args[2]);
		
		//parse the input data files
		HashMap<String, BigInteger> corpusMap = constructCorpusFromFiles(inputDirectory.listFiles());
		
		//construct the language model
		NGramFactory factory = new NGramFactory(n, corpusMap);
		MarkovNode<String> model = factory.constructModel();
		
		//generate gibberish with the language model
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
	
	private static HashMap<String, BigInteger> constructCorpusFromFiles(File[] files)
	{
		HashMap<String, BigInteger> ret = new HashMap<String, BigInteger>();
		
		for(File file : files)
		{
			String data = null;
			
			try
			{
				data = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				continue;
			}
			
			augmentCorpusWithString(ret, data);
		}
		
		return ret;
	}
	
	private static HashMap<String, BigInteger> augmentCorpusWithString(HashMap<String, BigInteger> corpus, String augment)
	{
		//for each word in the corpus
		String[] lines = augment.split("\n");
		for(String line : lines)
		{
			//extract the word and count from the formatted line
			int tabLocation = line.indexOf('\t');
			String word = line.substring(0, tabLocation);
			BigInteger count = new BigInteger(line.substring(tabLocation + 1));
			
			corpus.put(word, count);
		}
		
		return corpus;
	}
	
	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
}
