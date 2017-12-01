package inputreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to read a dataset of a hand-measure-session from a csv-file and extract its data into a format more 
 * suitable for further evaluation
 * @author Ben Fürnrohr
 */
public class CsvDataReader {

	/** 
	 * Parses the csv-file located at the filename into a list of {@link HandData}-object
	 * @param filename the name of the file to parse
	 * @return a List of {@HandData}-objects containing the contents of the csv-file
	 */
	public List<HandData> parseIntoDataObject(String filename) {
		List<HandData> returnList = new ArrayList<HandData>();
		
		try {
			String workingDir = System.getProperty("user.dir");
			java.io.BufferedReader FileReader= new java.io.BufferedReader(new java.io.FileReader(new java.io.File(workingDir+filename)));
			String line="";
			//discard first 200 lines to remove header and initial spikes
			for (int i = 0; i < 200; i++)
			{
				FileReader.readLine();
			}
			//read input
			while(null!=(line=FileReader.readLine())){ 
				// replace commas with decimal-points
				line = line.replace(',','.');
				String[] split=line.split(";");
				//check for validity
				if (Integer.parseInt(split[3])==1) {
					HandData currentData = new HandData(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[4]), Double.parseDouble(split[5]));
					returnList.add(currentData);
				}
				}
			FileReader.close();
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		return returnList;
		}
	}