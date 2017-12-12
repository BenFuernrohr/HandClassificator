package classification;

import java.util.ArrayList;
import java.util.List;

import inputreader.CalibrationDataset;
import inputreader.HandData;

/**
 * Classificator to analyse the number of movement-corrections within one session.
 * It interprets every local maximum of movement as a point, the hand moved to.
 * It then counts the number of times 2 local maxima are very close to each other, which will be interpreted as a movement-correction.
 * Before interpreting the movement the classificator will "smooth" the line by ruling out extremely minor variations.
 * A high count will mean a bad ({@link ClassificationResult.HIGH}) result.
 * @author Ben Fürnrohr
 */
public class MovementCorrectionClassificator extends AbsClassificator{

	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.LOW} */
	private static final int lowThreshold = 50;
	
	/** Threshold of relation between average and standard deviation until which result will be considered {@link ClassificationResult.MEDIUM} */
	private static final int mediumThreshold = 100;
	
	/** Smooth-anmount */
	private static final int smoothWidth = 10;
	
	/** Time-frame to accord for a "correction" instead for just a new movement */
	private static final int correctionWidth = 10;
	
	/** Correction of Y-Count */
	private static final int yCorrection = 3;
	
	/** Enum allowing to distinguish between movement in X or Y - Direction  */
	private enum Direction {X,Y}
	
	public MovementCorrectionClassificator(CalibrationDataset calibrationDataset) {
		super(calibrationDataset);
	}

	@Override
	public ClassificationResult classify() {
		int totalMovements = this.countMovementCorrections(Direction.X) + this.countMovementCorrections(Direction.Y) / yCorrection;
		
		if (totalMovements < MovementCorrectionClassificator.lowThreshold) {
			return ClassificationResult.LOW;
		}
		else if (totalMovements < MovementCorrectionClassificator.mediumThreshold) {
			return ClassificationResult.MEDIUM;
		}
		else {
			return ClassificationResult.HIGH;
		}		
	}	
	
	/**
	 * Counts the number of correction over the time in the movement in a certain direction
	 * @param direction {@link Direction} of the movement to be analyzed (X or Y)
	 * @return number of movement-corrections in this direction
	 */
	private int countMovementCorrections(Direction direction) {
		//get the position-data and smooth it
		List<Double> posList = new ArrayList<Double>();
		for (HandData hd : sessionData) {
			switch (direction) {
				case X: posList.add(hd.getPalm_Position_X()); break;
				case Y: posList.add(hd.getPalm_Position_Y()); break;
			}			
		}
		posList = this.applySmooth(posList);
		
		//find local minima and local maxima
		//initialize rising from first 2 values;
		boolean rising = posList.get(0) - posList.get(1) < 0 ? true : false;
			
		List<Integer> extremaList = new ArrayList<Integer>();
		
		double current = posList.get(0);
		int counter = 0;
		for(Double d : posList) {			
			if (rising & current > d) {
				//maximum found!
				extremaList.add(counter);
				rising = false;
			}
			else if (!rising & current < d) {
				//minimum found!
				rising = true;
				extremaList.add(counter);
			}
			counter++;
			current = d;
		}
		
		//check all the extrema. If a pair of minima/maxima are very close, it´s a movement-correction
		int movementCorrectionCounter = 0;
		//buffer an extremum
		int buffer = extremaList.get(0);
		for (int i = 1; i < extremaList.size()-1; i++) {
			int currentExtremum = extremaList.get(i);
			if (currentExtremum - buffer < MovementCorrectionClassificator.correctionWidth) {
				movementCorrectionCounter++;
			}
			buffer = currentExtremum;
		}
		return movementCorrectionCounter;
	}
	
	/**
	 * Smooths a list of doubles by removing small variations
	 * @param valueArray the array to be smoothed
	 * @param smoothWidth the width to smooth by
	 * @return ArrayList<Double> the smoothed array
	 */
	private ArrayList<Double> applySmooth(List<Double> valueArray) {

		ArrayList<Double> smoothedArray = new ArrayList<>();
		for (int i = 0; i < smoothWidth; i++) {
			smoothedArray.add(valueArray.get(smoothWidth));
		}
		for (int i = smoothWidth; i < valueArray.size() - 1 - smoothWidth; i++) {
			double avg = 0;
			for (int j = 1; j <= smoothWidth; j++) {
				avg = avg + valueArray.get(i - j);
				avg = avg + valueArray.get(i + j);
				avg = avg + valueArray.get(i);
			}
			avg = avg / (2 * smoothWidth + 1);
			smoothedArray.add(avg);
		}
		for (int i = 0; i < smoothWidth; i++) {
			smoothedArray.add(valueArray.get((valueArray.size() - smoothWidth)));
		}
		return smoothedArray;
	}
}