package inputreader;

import java.util.ArrayList;
import java.util.List;

public class CsvDataReader {

	
	public CsvDataReader() {
		}
	
	public List<HandData> parseIntoDataObject(String filename) {
		List<HandData> returnList = new ArrayList<HandData>();
		
		try {
			String workingDir = System.getProperty("user.dir");
			java.io.BufferedReader FileReader= new java.io.BufferedReader(new java.io.FileReader(new java.io.File(workingDir+filename)));
			String line="";
			//discard first 100 lines to remove Header and initial spikes
			for (int i = 0; i < 200; i++)
			{
				FileReader.readLine();
			}
			while(null!=(line=FileReader.readLine())){ 
				// replace commas with decimal-points
				line = line.replace(',','.');
				String[] split=line.split(";");
				
				HandData currentData = new HandData(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Integer.parseInt(split[3]), Double.parseDouble(split[4]), Double.parseDouble(split[5]));
				returnList.add(currentData);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		return returnList;
		}
	}