package FilePreProcessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

public class SingleFileProcessor {
	/**
	 * 
	 * @param args
	 * args[0] is input file name
	 * args[1] is output file name
	 */
	public static void main(String[] args)
	{
		//open the source file
		BufferedReader source = null;
		try {
			source = new BufferedReader(new FileReader(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		//extract the word counts
		Map<String, BigInteger> wordCounts = null;
		try {
			wordCounts = PreProcessor.extractRawWordCountsFromGoogleBooksFile(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//close the source file
		try {
			source.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//open target file
		BufferedWriter target = null;
		try {
			target = new BufferedWriter(new FileWriter(args[1]));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//write word counts
		for(Entry<String, BigInteger> entry : wordCounts.entrySet())
		{
			try {
				target.write(entry.getKey() + "\t" + entry.getValue() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//close target file
		try {
			target.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
