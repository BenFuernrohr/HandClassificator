package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

/**
 * Classificator to analyse the number of multiple thumbspreads in close proximity, representing a "double shot" within one session 
 * It counts the anmount of times the spread-field hits the degree-valued calibrated to represent a spread hand.
 * A high count will mean a bad ({@link ClassificationResult.HIGH}) result.
 * @author Ben Fürnrohr
 */
public class MultipleThumbSpreadClassificator extends AbsClassificator{
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.LOW} */
	private static final int lowThreshold = 3;
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.MEDIUM} */
	private static final int mediumThreshold = 6;

	/** Since spreads in the actual game are usually not as high as in the calibration, the calibrated threshold will be reduced by this multiplicator*/
	private static final double measurementCorrection = 0.7;
	
	/** Interval between spreads up to which it is considered a "double-Spread" */
	private static final int doubleSpreadInterval = 50;

	public MultipleThumbSpreadClassificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}
	
	@Override
	public ClassificationResult classify() {

		//start with relaxed thumb, no recent spreads
		boolean thumbIsRelaxed = true;
		boolean thumbWasRecentlySpread = false;
		int doubleThumbSpreadCount = 0;
		int counter = 0;
		for (HandData hd: sessionData) {
			//hit calibrated spread-level? 
			//Correct avg to 50%, because the calibration means a very high spread but in sessions spreads are not that big
			if (hd.getThumb() > this.calibrationDataset.getAvgThumbSpread()*MultipleThumbSpreadClassificator.measurementCorrection ) 
			{
				if (thumbIsRelaxed) {
					//it's a spread
					thumbIsRelaxed = false;
					counter = 0;
					if (thumbWasRecentlySpread) {
						//it´s a double-spread
						doubleThumbSpreadCount++;
					}
					thumbWasRecentlySpread = true;
				}
			}
			else if (hd.getThumb() < this.calibrationDataset.getAvgThumbRest()) {
				if (!thumbIsRelaxed) {
					//it's relaxed again
					thumbIsRelaxed = true;
				}
				else if (thumbIsRelaxed) {
					//count the duration of relaxed h0and. Reset recentlySpread if hand is relaxed for long enough
					counter++;
					if (counter > MultipleThumbSpreadClassificator.doubleSpreadInterval)
						thumbWasRecentlySpread = false;
				}
			}
		}
		//classify according to number of spreads
		if (doubleThumbSpreadCount < MultipleThumbSpreadClassificator.lowThreshold) {
			return ClassificationResult.LOW;
		}
		else if (doubleThumbSpreadCount < MultipleThumbSpreadClassificator.mediumThreshold) {
			return ClassificationResult.MEDIUM;
		}
		else {
			return ClassificationResult.HIGH;
		}
	}
	
}