package evaluation;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import classification.Classification_Result;
import dempster.DempsterHandler;
import dempster.Measure;

/**
 * Class to handle the evaluation of classificator-results using the Dempster-Shafer-method
 * @author Ben Fürnrohr
 */
public class DempsterEvaluator {
	
	/** Lists representing all possible classifications */
	private static final List<Integer> BEGINNER_LIST = Arrays.asList(new Integer[] {1,0,0});
	private static final List<Integer> EXPERT_OR_ADVANCED_LIST = Arrays.asList(new Integer[] {0,1,1});
	private static final List<Integer> BEGINNER_OR_ADVANCED_LIST = Arrays.asList(new Integer[] {1,1,0});
	private static final List<Integer> COULD_BE_ANYTHING_LIST = Arrays.asList(new Integer[]{1,1,1});
	
	/** Array of string representing the single results */
	private static final String[] RESULT_NAMES = {"Beginner", "Advanced", "Expert"};

	/**
	 * Evaluates classification-result and prints the result in form of beliefs, plausabilitys and doubts for each case
	 * @param zAxis result of the z-axis-variation-classification
	 * @param movCor result of the movement-correction-classification
	 * @param multThumb result of the multiple-rapid-thumb-spreads-classification
	 * @param handSpread result of the handspread-count-Classification
	 */
	public void evaluateClassification(Classification_Result zAxis,
			Classification_Result movCor, Classification_Result multThumb, Classification_Result handSpread) {
		DempsterHandler dempsterHandler = new DempsterHandler(3);
		
		// z-Axis
		Measure zAxisMeasure = dempsterHandler.addMeasure();
		switch (zAxis) {
			case LOW: zAxisMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7); break;
			case MEDIUM: zAxisMeasure.addEntry(BEGINNER_OR_ADVANCED_LIST, 0.7); break;
			case HIGH: zAxisMeasure.addEntry(BEGINNER_LIST, 0.7); break;
		}

		// handSpread
		Measure handSpreadMeasure = dempsterHandler.addMeasure();
		switch (handSpread) {
			case LOW: handSpreadMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7); break;
			case MEDIUM: handSpreadMeasure.addEntry(COULD_BE_ANYTHING_LIST, 0.7); break;
			case HIGH: handSpreadMeasure.addEntry(BEGINNER_LIST, 0.7); break;
		}

		// movementCorrection
		Measure movementCorrectionMeasure = dempsterHandler.addMeasure();
		switch (movCor) {
			case LOW: movementCorrectionMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7); break;
			case MEDIUM: movementCorrectionMeasure.addEntry(BEGINNER_OR_ADVANCED_LIST, 0.7); break;
			case HIGH: movementCorrectionMeasure.addEntry(BEGINNER_LIST, 0.7); break;
		}

		// doubleThumbSpread
		Measure doubleThumbSpreadMeasure = dempsterHandler.addMeasure();
		switch (multThumb) {
			case LOW: doubleThumbSpreadMeasure.addEntry(EXPERT_OR_ADVANCED_LIST, 0.7); break;
			case MEDIUM: doubleThumbSpreadMeasure.addEntry(BEGINNER_OR_ADVANCED_LIST, 0.7); break;
			case HIGH: doubleThumbSpreadMeasure.addEntry(BEGINNER_LIST, 0.7); break;
		}
				
		dempsterHandler.accumulateAllMeasures();
		this.interpretMeasure(dempsterHandler.getFirstMeasure());
	}

	/**
	 * Interprets the {@link Measure} that is the result of the dempster-evaluation and prints the result
	 * @param measure the measure to be interpreted
	 */
	private void interpretMeasure(Measure measure) {
		System.out.println("Results of Dempster-Shafer-Evaluation: \n");
		
		//find the result with the highest belief
		int withHighestBelief = 0;
		double highestBelief = 0;
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		//print all the results
		for (int i = 0; i < 3; i++) {
			double belief = measure.calculateBelief(i);
			double doubt = measure.calculateDoubt(i);
			double plausability = measure.calculatePlausability(i);
			if (belief > highestBelief) {
				highestBelief = belief;
				withHighestBelief = i;
			}
			String entryString = "Results for " + DempsterEvaluator.RESULT_NAMES[i] + ":\nPlausability: \t" + df.format(plausability) + "\tBelief:  \t" + df.format(belief) + "\tDoubt:  \t" + df.format(doubt) + "\n";
			System.out.println(entryString);
		}
		
		//print the final verdict
		String finalResultString = "Final verdict: You are " + DempsterEvaluator.RESULT_NAMES[withHighestBelief] + " with a probability (belief) of " + df.format(highestBelief);
		System.out.println(finalResultString +"\n\n");
	}
	

}