package classification;

import inputreader.CalibrationDataset;
import inputreader.HandData;

public class HandSpreadCount_Classificator extends Abs_Classificator{

	public HandSpreadCount_Classificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public Classification_Result classify() {
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
		if (handSpreadCount < 3) {
			return Classification_Result.LOW;
		}
		else if (handSpreadCount < 6) {
			return Classification_Result.MEDIUM;
		}
		else {
			return Classification_Result.HIGH;
		}
	}
	
}