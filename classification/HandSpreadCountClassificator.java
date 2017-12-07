package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

/**
 * Classificator to analyse the number of handspreads within one session. 
 * It counts the anmount of times the thumb-field hits the degree-valued calibrated to represent a spread thumb within a short period of time. 
 * A high count will mean a bad ({@link ClassificationResult.HIGH}) result.
 * @author Ben Fürnrohr
 */
public class HandSpreadCountClassificator extends AbsClassificator{

	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.LOW} */
	private static final int lowThreshold = 3;
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.MEDIUM} */
	private static final int mediumThreshold = 6;
	
	public HandSpreadCountClassificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public ClassificationResult classify() {
		//start with relaxed hand
		boolean handIsRelaxed = true;
		int handSpreadCount = 0;
		for (HandData hd: sessionData) {
			//hit calibrated spread-level?
			if (hd.getSpread() > this.calibrationDataset.getAvgHandSpread()) {
				if (handIsRelaxed) {
					//it's a spread
					handSpreadCount++;
					handIsRelaxed = false;
				}
			}
			else if (hd.getSpread() < this.calibrationDataset.getAvgHandRest()) {
				if (!handIsRelaxed) {
					//it's relaxed again
					handIsRelaxed = true;
				}
			}
		}		
		//classify according to number of spreads
		if (handSpreadCount < HandSpreadCountClassificator.lowThreshold) {
			return ClassificationResult.LOW;
		}
		else if (handSpreadCount < HandSpreadCountClassificator.mediumThreshold) {
			return ClassificationResult.MEDIUM;
		}
		else {
			return ClassificationResult.HIGH;
		}
	}
	
}