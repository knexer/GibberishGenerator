package FilePreProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class PreProcessor {
	
	private static String[] tags = {
		"_NOUN",
		"_VERB",
		"_ADJ",
		"_ADV",
		"_PRON",
		"_DET",
		"_ADP",
		"_NUM",
		"_CONJ",
		"_PRT",
		"_X",
		"_."
	};
	
	public static Map<String, BigInteger> extractRawWordCountsFromGoogleBooksFile(BufferedReader source) throws IOException
	{
		Map<String, BigInteger> ret = new HashMap<String, BigInteger>();

		//for each line in the input
		while(source.ready())
		{
			//parse the line
			String line = source.readLine();
			String[] tokens = line.split("\t");
			String word = tokens[0];//the word is the first thing on the line
			BigInteger count = new BigInteger(tokens[2]);//the count is the third thing
			
			//if the word is tagged, we'd be double-counting to include it - so don't

			//update the map
			if(!isUntagged(word)) continue;
			if(ret.containsKey(word))
			{
				ret.put(word, ret.get(word).add(count));
			}
			else
			{
				ret.put(word, count);
			}
		}
		
		return ret;
	}
	
	private static boolean isUntagged(String word)
	{
		for(String tag : tags)
		{
			if(word.endsWith(tag)) return false;
		}
		return true;
	}
}
