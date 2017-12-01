package evaluation;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import classification.Classification_Result;
import dempster.DempsterHandler;
import dempster.Measure;

public class DempsterEvaluator {
	
	/** Lists representing all possible classifications */
	private static final List<Integer> BEGINNER_LIST = Arrays.asList(new Integer[] {1,0,0});
	private static final List<Integer> EXPERT_OR_ADVANCED_LIST = Arrays.asList(new Integer[] {0,1,1});
	private static final List<Integer> BEGINNER_OR_ADVANCED_LIST = Arrays.asList(new Integer[] {1,1,0});
	private static final List<Integer> COULD_BE_ANYTHING_LIST = Arrays.asList(new Integer[]{1,1,1});
	
	/** Array of string representing the single results */
	private static final String[] RESULT_NAMES = {"Beginner", "Advanced", "Expert"};

	public void evaluateClassification(DempsterHandler dempsterHandler, Classification_Result zAxis,
			Classification_Result movCor, Classification_Result multThumb, Classification_Result handSpread) {
		//Show results
		System.out.println("zAxis-Variation: " + zAxis);
		System.out.println("handSpread-Count: " + handSpread);
		System.out.println("doubleThumbSpread-Count: " + multThumb);
		System.out.println("movementCorrection-Count: " + movCor + "\n");
		
		// z-Axis
		Measure zAxisMeasure = dempsterHandler.addMeasure();
		if (zAxis.equals(Classification_Result.LOW)) {
			zAxisMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7);
		} else if (zAxis.equals(Classification_Result.MEDIUM)) {
			zAxisMeasure.addEntry(BEGINNER_OR_ADVANCED_LIST, 0.7);
		} else if (zAxis.equals(Classification_Result.HIGH)) {
			zAxisMeasure.addEntry(BEGINNER_LIST, 0.7);
		}

		// handSpread
		Measure handSpreadMeasure = dempsterHandler.addMeasure();
		if (handSpread.equals(Classification_Result.LOW)) {
			handSpreadMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7);
		} else if (handSpread.equals(Classification_Result.MEDIUM)) {
			handSpreadMeasure.addEntry(COULD_BE_ANYTHING_LIST, 0.7);
		} else if (handSpread.equals(Classification_Result.HIGH)) {
			handSpreadMeasure.addEntry(BEGINNER_LIST, 0.7);
		}

		// movementCorrection
		Measure movementCorrectionMeasure = dempsterHandler.addMeasure();
		if (movCor.equals(Classification_Result.LOW)) {
			movementCorrectionMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7);
		} else if (movCor.equals(Classification_Result.MEDIUM)) {
			movementCorrectionMeasure.addEntry(BEGINNER_OR_ADVANCED_LIST, 0.7);
		} else if (movCor.equals(Classification_Result.HIGH)) {
			movementCorrectionMeasure.addEntry(BEGINNER_LIST, 0.7);
		}

		// doubleThumbSpread
		Measure doubleThumbSpreadMeasure = dempsterHandler.addMeasure();
		if (multThumb.equals(Classification_Result.LOW)) {
			doubleThumbSpreadMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7);
		} else if (multThumb.equals(Classification_Result.MEDIUM)) {
			doubleThumbSpreadMeasure.addEntry(BEGINNER_OR_ADVANCED_LIST, 0.7);
		} else if (multThumb.equals(Classification_Result.HIGH)) {
			doubleThumbSpreadMeasure.addEntry(BEGINNER_LIST, 0.7);
		}
		
		dempsterHandler.accumulateAllMeasures();
	}

	public void interpretMeasure(Measure measure) {
		int withHighestBelief = 0;
		double highestBelief = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		for (int i = 0; i < 3; i++) {
			double belief = measure.calculateBelief(i);
			double doubt = measure.calculateDoubt(i);
			double plausability = measure.calculatePlausability(i);
			if (belief > highestBelief) {
				highestBelief = belief;
				withHighestBelief = i;
			}
			String entryString = "Results for " + DempsterEvaluator.RESULT_NAMES[i] + ":\nPlausability: \t" + df.format(plausability) + "\nBelief:  \t" + df.format(belief) + "\nDoubt:  \t" + df.format(doubt) + "\n";
			System.out.println(entryString);
		}
		String finalResultString = "Final evaluation: You are " + DempsterEvaluator.RESULT_NAMES[withHighestBelief] + " with a propability (belief) of " + df.format(highestBelief);
		System.out.println(finalResultString +"\n\n");
	}
	

}