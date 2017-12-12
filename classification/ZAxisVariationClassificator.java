package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

/**
 * Classificator to analyse the level of movement in z-axis-directions within one session 
 * It uses the relation between average and standard deviation. 
 * A high standard deviation will mean a bad ({@link ClassificationResult.HIGH}) result.
 * @author Ben Fürnrohr
 */
public class ZAxisVariationClassificator extends AbsClassificator {
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.LOW} */
	private static final double lowThreshold = 0.3;
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.MEDIUM} */
	private static final double mediumThreshold = 0.6;	
	
	public ZAxisVariationClassificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public ClassificationResult classify() {
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
		if (relation < ZAxisVariationClassificator.lowThreshold) {
			return ClassificationResult.LOW;
		}
		else if (relation < ZAxisVariationClassificator.mediumThreshold) {
			return ClassificationResult.MEDIUM;
		}
		else {
			return ClassificationResult.HIGH;
		}		
	}
}