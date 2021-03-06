package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

/**
 * Classificator to analyse the number of handspreads within one session. 
 * It counts the anmount of times the thumb-field hits the degree-valued calibrated to represent a spread thumb within a short period of time. 
 * A high count will mean a bad ({@link ClassificationResult.HIGH}) result.
 * @author Ben F�rnrohr
 */
public class HandSpreadCountClassificator extends AbsClassificator{

	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.LOW} */
	private static final int lowThreshold = 4;
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.MEDIUM} */
	private static final int mediumThreshold = 8;
	
	/** Since spreads in the actual game are usually not as high as in the calibration, the calibrated threshold will be reduced by this multiplicator*/
	private static final double measurementCorrection = 0.8;
	
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
			if (hd.getSpread() > this.calibrationDataset.getAvgHandSpread()*HandSpreadCountClassificator.measurementCorrection) {
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