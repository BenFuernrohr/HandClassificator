package classification;

import java.util.List;

import inputreader.CalibrationDataset;
import inputreader.HandData;

public abstract class Abs_Classificator implements Classifyable{
	
	protected CalibrationDataset calibrationDataset;
	
	protected List<HandData> sessionData;
	
	public Abs_Classificator(CalibrationDataset calibrationDataset) {
		this.calibrationDataset = calibrationDataset;
	}

	public void setSessionData(List<HandData> sessionData) {
		this.sessionData = sessionData;
	}
	
	
}