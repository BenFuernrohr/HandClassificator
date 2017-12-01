package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

public class MultipleThumbSpread_Classificator extends Abs_Classificator{

	public MultipleThumbSpread_Classificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public Classification_Result classify() {

		//start with relaxed thumb, no recent spreads
		boolean thumbIsRelaxed = true;
		boolean thumbWasRecentlySpread = false;
		int doubleThumbSpreadCount = 0;
		int counter = 0;
		for (HandData hd: sessionData) {
			//hit calibrated spread-level?
			if (hd.getThumb() > this.calibrationDataset.getAvgThumbSpread()/2) 
			//TODO: find solution to calibration-reality-offset of thumb-Spread
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
					//count the duration of relaxed Hand. Reset recentlySpread if hand is relaxed for long enough
					counter++;
					if (counter >50)
						thumbWasRecentlySpread = false;
				}
			}
		}
		//classify according to number of spreads
		if (doubleThumbSpreadCount < 3) {
			return Classification_Result.LOW;
		}
		else if (doubleThumbSpreadCount < 6) {
			return Classification_Result.MEDIUM;
		}
		else {
			return Classification_Result.HIGH;
		}
	}
	
}