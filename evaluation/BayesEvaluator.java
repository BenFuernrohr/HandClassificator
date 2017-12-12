package evaluation;

import java.text.DecimalFormat;

import classification.ClassificationResult;
import norsys.netica.Environ;
import norsys.netica.Net;
import norsys.netica.NeticaException;
import norsys.netica.Streamer;
import norsys.neticaEx.aliases.Node;

/**
 * Class to handle the evaluation of classificator-results using a Bayes-net provided by the Netica-framework
 * @author Ben Fürnrohr
 */
public class BayesEvaluator{
	
	/** The bayes-network */
	private Net net;

	/** Nodes representing results of the classification */
	private Node  zAxisVariation;
	private Node  movementCorrection;
	private Node  multipleThumbSpreads;
	private Node  handSpreads;
	
	/** Node representing the skill-level of the player */
	private Node  skillLevel;
	
	/** Array of string representing the single results */
	private static final String[] RESULT_NAMES = {"Beginner", "Advanced", "Expert"};
	
	
	public BayesEvaluator() {
		
		//initialize the net
		try {
			Node.setConstructorClass ("norsys.neticaEx.aliases.Node"); 
			new Environ (null);
	
			this.net = new Net();
	
			this.zAxisVariation = new Node ("zAxisVariation", "high,medium,low", net);
			this.movementCorrection = new Node ("movCorrection", "high,medium,low", net);
			this.multipleThumbSpreads = new Node ("multipleThumbSpreads", "high,medium,low", net);
			this.handSpreads = new Node ("handSpreads", "high,medium,low", net);
			this.skillLevel  = new Node ("SkillLevel", "Beginner,Advanced,Expert", net);
	
			this.skillLevel.addLink (this.zAxisVariation);
			this.skillLevel.addLink (this.movementCorrection);
			this.skillLevel.addLink (this.multipleThumbSpreads);
			this.skillLevel.addLink (this.handSpreads);
	
			float[] basicprops = { 0.33F, 0.33F , 0.34F};
			int[] basicParentStates = null;
			this.zAxisVariation.setCPTable ( basicParentStates, basicprops );
			this.movementCorrection.setCPTable ( basicParentStates, basicprops );
			this.multipleThumbSpreads.setCPTable ( basicParentStates, basicprops );
			this.handSpreads.setCPTable ( basicParentStates, basicprops );
			
			this.skillLevel.setCPTable(this.skillLevelProbs);
			
			this.net.compile();
		}
		catch (NeticaException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Evaluates classification-result and prints the result in form of a belief 
	 * @param zAxis result of the z-axis-variation-classification
	 * @param movCor result of the movement-correction-classification
	 * @param multThumb result of the multiple-rapid-thumb-spreads-classification
	 * @param handSpread result of the handspread-count-Classification
	 */
	public void evaluateClassification(ClassificationResult zAxis,
			ClassificationResult movCor, ClassificationResult multThumb, ClassificationResult handSpread) {
		try {
			//retract findings if there are any
			this.net.retractFindings();
			
			//enter states according to findings 
			zAxisVariation.finding().enterState(translateClassificationResult(zAxis));
			movementCorrection.finding().enterState(translateClassificationResult(movCor));
			multipleThumbSpreads.finding().enterState(translateClassificationResult(multThumb));
			handSpreads.finding().enterState(translateClassificationResult(handSpread));
			
			System.out.println("Results of Bayes-Net-Evaluation: \n");
			int withHighestBelief = 0;
			double highestBelief = 0;
			DecimalFormat df = new DecimalFormat("#.##");
			for (int i = 0; i < 3; i++) {
				double belief = skillLevel.getBelief(BayesEvaluator.RESULT_NAMES[i]);
				if (belief > highestBelief) {
					highestBelief = belief;
					withHighestBelief = i;
				}
				String entryString = "Belief for " + BayesEvaluator.RESULT_NAMES[i] +  ": \t" + df.format(belief) ;
				System.out.println(entryString);
			}
			String finalResultString = "Final verdict: You are " + BayesEvaluator.RESULT_NAMES[withHighestBelief] + " with a probability (belief) of " + df.format(highestBelief);
			System.out.println(finalResultString +"\n");
			
			//write the net to .dne-file 
			String fileName = "BayesResults/Bayes-Net"+System.currentTimeMillis()+ BayesEvaluator.RESULT_NAMES[withHighestBelief]+ df.format(highestBelief) +".dne";
			Streamer stream = new Streamer(fileName); 
			this.net.write(stream);
			
			System.out.println("The Bayes-Net has been written to " + fileName + "\n");
		}
		catch(NeticaException e) {
			e.printStackTrace();
		}
	}
	
	/** Array of probabilities to be used as a CPTable for SkillLevel
	 *  The Values are critical for the results of the net
	 * 4 parents with 3 states each, makes 3^4 = 81 lines of probabilities. 3 probabilities per line
	 * */
	private final float[] skillLevelProbs = {
	//z mov thum hand			 B	     A		E
	/*h  h   h    h */			0.99F, 0.01F, 0.00F, 
	/*h  h   h    m */			0.90F, 0.05F, 0.05F, 
	/*h  h   h    l */			0.85F, 0.09F, 0.06F, 
	/*h  h   m    h */			0.95F, 0.04F, 0.01F, 
	/*h  h   m    m */			0.88F, 0.10F, 0.02F, 
	/*h  h   m    l */			0.81F, 0.11F, 0.08F, 
	/*h  h   l    h */			0.70F, 0.25F, 0.05F, 
	/*h  h   l    m */			0.55F, 0.38F, 0.07F, 
	/*h  h   l    l */			0.40F, 0.50F, 0.10F, 
	/*h  m   h    h */			0.95F, 0.04F, 0.01F, 
	/*h  m   h    m */			0.84F, 0.10F, 0.06F,
	/*h  m   h    l */			0.81F, 0.11F, 0.08F, 
	/*h  m   m    h */			0.88F, 0.10F, 0.02F,
	/*h  m   m    m */			0.75F, 0.20F, 0.05F, 
	/*h  m   m    l */			0.60F, 0.30F, 0.10F, 
	/*h  m   l    h */			0.70F, 0.18F, 0.12F, 
	/*h  m   l    m */			0.50F, 0.30F, 0.20F, 
	/*h  m   l    l */			0.36F, 0.40F, 0.24F, 
	/*h  l   h    h */			0.87F, 0.08F, 0.05F, 
	/*h  l   h    m */			0.73F, 0.18F, 0.09F, 
	/*h  l   h    l */			0.48F, 0.40F, 0.12F,  
	/*h  l   m    h */			0.70F, 0.20F, 0.10F, 
	/*h  l   m    m */			0.60F, 0.28F, 0.12F, 
	/*h  l   m    l */			0.48F, 0.40F, 0.12F, 
	/*h  l   l    h */			0.30F, 0.50F, 0.20F, 
	/*h  l   l    m */			0.35F, 0.35F, 0.30F, 
	/*h  l   l    l */			0.30F, 0.30F, 0.40F, 
	/*m  h   h    h */			0.90F, 0.09F, 0.01F, 
	/*m  h   h    m */			0.80F, 0.15F, 0.05F, 
	/*m  h   h    l */			0.75F, 0.19F, 0.06F, 
	/*m  h   m    h */			0.85F, 0.14F, 0.01F, 
	/*m  h   m    m */			0.78F, 0.15F, 0.07F, 
	/*m  h   m    l */			0.71F, 0.21F, 0.08F, 
	/*m  h   l    h */			0.55F, 0.35F, 0.10F, 
	/*m  h   l    m */			0.45F, 0.42F, 0.13F, 
	/*m  h   l    l */			0.30F, 0.50F, 0.20F, 
	/*m  m   h    h */			0.85F, 0.14F, 0.01F, 
	/*m  m   h    m */			0.74F, 0.20F, 0.06F,
	/*m  m   h    l */			0.51F, 0.25F, 0.24F, 
	/*m  m   m    h */			0.78F, 0.20F, 0.02F,
	/*m  m   m    m */			0.65F, 0.25F, 0.10F, 
	/*m  m   m    l */			0.50F, 0.38F, 0.12F, 
	/*m  m   l    h */			0.40F, 0.35F, 0.25F, 
	/*m  m   l    m */			0.40F, 0.38F, 0.22F, 
	/*m  m   l    l */			0.26F, 0.45F, 0.29F, 
	/*m  l   h    h */			0.77F, 0.18F, 0.05F, 
	/*m  l   h    m */			0.63F, 0.28F, 0.09F, 
	/*m  l   h    l */			0.38F, 0.45F, 0.17F,  
	/*m  l   m    h */			0.60F, 0.28F, 0.12F, 
	/*m  l   m    m */			0.47F, 0.36F, 0.17F, 
	/*m  l   m    l */			0.38F, 0.48F, 0.14F, 
	/*m  l   l    h */			0.20F, 0.50F, 0.30F, 
	/*m  l   l    m */			0.25F, 0.37F, 0.38F, 
	/*m  l   l    l */			0.20F, 0.35F, 0.45F,  
	/*l  h   h    h */			0.69F, 0.27F, 0.04F, 
	/*l  h   h    m */			0.60F, 0.30F, 0.10F, 
	/*l  h   h    l */			0.55F, 0.36F, 0.09F, 
	/*l  h   m    h */			0.65F, 0.30F, 0.05F, 
	/*l  h   m    m */			0.57F, 0.35F, 0.08F, 
	/*l  h   m    l */			0.51F, 0.31F, 0.18F, 
	/*l  h   l    h */			0.40F, 0.40F, 0.20F, 
	/*l  h   l    m */			0.25F, 0.48F, 0.27F, 
	/*l  h   l    l */			0.15F, 0.50F, 0.35F, 
	/*l  m   h    h */			0.65F, 0.30F, 0.05F, 
	/*l  m   h    m */			0.54F, 0.35F, 0.11F,
	/*l  m   h    l */			0.51F, 0.36F, 0.13F, 
	/*l  m   m    h */			0.48F, 0.36F, 0.16F,
	/*l  m   m    m */			0.45F, 0.45F, 0.10F, 
	/*l  m   m    l */			0.20F, 0.52F, 0.28F, 
	/*l  m   l    h */			0.40F, 0.33F, 0.27F, 
	/*l  m   l    m */			0.20F, 0.50F, 0.30F, 
	/*l  m   l    l */			0.16F, 0.40F, 0.44F, 
	/*l  l   h    h */			0.57F, 0.34F, 0.09F, 
	/*l  l   h    m */			0.43F, 0.38F, 0.19F, 
	/*l  l   h    l */			0.18F, 0.40F, 0.42F,  
	/*l  l   m    h */			0.40F, 0.40F, 0.20F, 
	/*l  l   m    m */			0.30F, 0.38F, 0.32F, 
	/*l  l   m    l */			0.18F, 0.40F, 0.42F, 
	/*l  l   l    h */			0.10F, 0.50F, 0.40F, 
	/*l  l   l    m */			0.08F, 0.45F, 0.47F, 
	/*l  l   l    l */			0.05F, 0.35F, 0.60F,   };

	/**
	 * Transfers a value from a classification-result to a representing string
	 * @param classificationResult the classification-result
	 * @return a String representing the classification-result
	 */
	private String translateClassificationResult(ClassificationResult classificationResult) {
		switch (classificationResult) {
			case LOW: return "low";
			case MEDIUM: return "medium";
			case HIGH: return "high";
			default: return "low";
		}
	}
}