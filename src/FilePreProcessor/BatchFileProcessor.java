package FilePreProcessor;

import java.io.File;

public class BatchFileProcessor {

	/**
	 * @param args
	 * args[0] directory with input files
	 * args[1] directory for output files
	 */
	public static void main(String[] args) {
		File inputDirectory = new File(args[0]);
		File outputDirectory = new File(args[1]);
		
		//for each file in the input directory
		for(File inputFile : inputDirectory.listFiles())
		{
			//compute corresponding destination filename
			String destinationFilename = computeDestinationFilename(inputFile.getName());
			
			//process the file
			SingleFileProcessor.processSingleFile(inputFile.getAbsolutePath(), outputDirectory.getAbsolutePath() + File.separator + destinationFilename);
		}
	}
	
	private static String computeDestinationFilename(String sourceFilename)
	{
		return sourceFilename.charAt(sourceFilename.length() - 1) + ".txt";
	}

}
