package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

public class ZAxisVariation_Classificator extends Abs_Classificator {
	
	public ZAxisVariation_Classificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public Classification_Result classify() {
		//generate session average
		double sum = 0;
		for (HandData hd : sessionData) {
			sum = sum + hd.getPalm_Position_Z();
		}
		double average = sum/sessionData.size();
		
		//generate standard deviation
		double standardDeviation = 0;
		double standardSum = 0;
		for (HandData hd : sessionData) {
			double diff = hd.getPalm_Position_Z() - average;
			diff = diff * diff;
			standardSum = standardSum + diff;
		}
		standardDeviation = Math.sqrt(standardSum/sessionData.size());
		
		//relation of standardDeviation and average gives the result
		double relation = standardDeviation/average;
		if (relation < 0.2) {
			return Classification_Result.LOW;
		}
		else if (relation < 0.4) {
			return Classification_Result.MEDIUM;
		}
		else {
			return Classification_Result.HIGH;
		}		
	}
}