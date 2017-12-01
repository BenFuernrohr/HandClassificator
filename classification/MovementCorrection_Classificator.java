package classification;

import java.util.ArrayList;
import java.util.List;

import inputreader.CalibrationDataset;
import inputreader.HandData;

public class MovementCorrection_Classificator extends Abs_Classificator{

	public MovementCorrection_Classificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public Classification_Result classify() {
		//find local minima and local maxima
		//initialize rising from first 2 values;		
		boolean rising = sessionData.get(0).getPalm_Position_X() - sessionData.get(1).getPalm_Position_X() < 0 ? true : false;
			
		List<Integer> extremaList = new ArrayList<Integer>();
		
		double current = sessionData.get(0).getPalm_Position_X();
		int counter = 0;
		for(HandData hd : sessionData) {
			double posX = hd.getPalm_Position_X();
			if (rising & current > posX) {
				//maximum found!
				extremaList.add(counter);
				rising = false;
			}
			else if (!rising & current < posX) {
				//minimum found!
				rising = true;
				extremaList.add(counter);
			}
			counter++;
			current = posX;
		}
		
		//check all the extrema. If a pair of minima/maxima are very close, it´s a movement-correction
		int movementCorrectionCounter = 0;
		//buffer an extremum
		int buffer = extremaList.get(0);
		for (int i = 1; i < extremaList.size()-1; i++) {
			int currentExtremum = extremaList.get(i);
			if (currentExtremum - buffer < 5) {
				movementCorrectionCounter++;
			}
			buffer = currentExtremum;
		}
		if (movementCorrectionCounter < 30) {
			return Classification_Result.LOW;
		}
		else if (movementCorrectionCounter < 60) {
			return Classification_Result.MEDIUM;
		}
		else {
			return Classification_Result.HIGH;
		}		
	}	
}