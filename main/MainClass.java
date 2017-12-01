package main;

import java.util.Arrays;
import java.util.List;

import classification.Classification_Result;
import classification.Classifyable;
import classification.HandSpreadCount_Classificator;
import classification.MovementCorrection_Classificator;
import classification.MultipleThumbSpread_Classificator;
import classification.ZAxisVariation_Classificator;
import dempster.DempsterHandler;
import dempster.Measure;
import evaluation.DempsterEvaluator;
import evaluation.BayesEvaluator;
import inputreader.CalibrationDataset;
import inputreader.CsvDataReader;
import inputreader.HandData;
import inputreader.RawDataHandler;

/**
 * Main Class of the Hand-Movement-Classificator as used in WB-IT15D (DHBW)
 * Contains the Main-Class that reads from the 3 csv-files and starts classification and evaluation of the movement
 * @author Ben Fürnrohr
 */
public class MainClass{
	
	/** Datareader for csv-Files */
	CsvDataReader csvDataReader = new CsvDataReader();
	
	/** Dataset from the Calibration done by initial calibration-session */
	private CalibrationDataset calibrationDataSet;
	
	/** Data from first Session */
	private List<HandData> firstSessionData;
	
	/** Data from second Session */
	private List<HandData> secondSessionData;
	
	/** Classificators */
	private Classifyable handSpreadCount;
	private Classifyable movementCorrection;
	private Classifyable multipleThumbSpread;
	private Classifyable zAxisVariation;
		
	/** Dempster-Evaluator */
	private DempsterEvaluator dempsterEvaluator = new DempsterEvaluator();	;	
	
	/** Netica-Evaluator */
	private BayesEvaluator bayesEvaluator = new BayesEvaluator();;
	
	public static void main(String args[]) {		
		
		MainClass mainClass = new MainClass();
		mainClass.createCalibrationDataset();
		mainClass.extractSessionData();	
		mainClass.initializeClassificators();
				
		//evaluate first Session
		System.out.println("Evaluating first Session:");
		mainClass.evaluateSession(mainClass.firstSessionData);
		
		//evaluate second Session
		System.out.println("Evaluating second Session:");
		mainClass.evaluateSession(mainClass.secondSessionData);	
	}
	
	/**
	 * Evaluate a session using the extracted session-Data
	 * @param sessionData the data of the session to be evaluated 
	 */
	private void evaluateSession(List<HandData> sessionData) {

		//set the session data for the classificators
		this.handSpreadCount.setSessionData(sessionData);
		this.movementCorrection.setSessionData(sessionData);
		this.multipleThumbSpread.setSessionData(sessionData);
		this.zAxisVariation.setSessionData(sessionData);
		
		Classification_Result handSpreadClassification = this.handSpreadCount.classify();
		Classification_Result movementCorrectionClassification = this.movementCorrection.classify();
		Classification_Result multipleThumbSpreadClassification = this.multipleThumbSpread.classify();
		Classification_Result zAxisVariationClassification = this.zAxisVariation.classify();		
		
		//Show results
		System.out.println("Classificator-Results: \n");
		System.out.println("zAxis-Variation: \t\t" + zAxisVariationClassification);
		System.out.println("handSpread-Count:  \t\t" + handSpreadClassification);
		System.out.println("doubleThumbSpread-Count: \t" + multipleThumbSpreadClassification);
		System.out.println("movementCorrection-Count: \t" + movementCorrectionClassification + "\n");

		dempsterEvaluator.evaluateClassification(zAxisVariationClassification, movementCorrectionClassification, multipleThumbSpreadClassification, handSpreadClassification);
		bayesEvaluator.evaluateClassification(zAxisVariationClassification, movementCorrectionClassification, multipleThumbSpreadClassification, handSpreadClassification);
	}

	/**
	 * Initialize the classificators with the calibration-dataset
	 */
	private void initializeClassificators() {
		handSpreadCount = new HandSpreadCount_Classificator(calibrationDataSet);
		movementCorrection = new MovementCorrection_Classificator(calibrationDataSet);
		multipleThumbSpread = new MultipleThumbSpread_Classificator(calibrationDataSet);
		zAxisVariation = new ZAxisVariation_Classificator(calibrationDataSet);
		
	}
	
	/**
	 * Extract the data from the csv-file used for calibration
	 */
	private void createCalibrationDataset() {
		List<HandData> extractedData = csvDataReader.parseIntoDataObject("/CSV_Data/sample_0_Statistics.csv");

		RawDataHandler rawDataHandler = new RawDataHandler();
		rawDataHandler.setDataFromCompleteRun(extractedData);
		this.calibrationDataSet = rawDataHandler.generateCalibrationDataset();
	}
	
	/**
	 * Extract the SessionData from the csv-Files
	 */
	private void extractSessionData() {
		this.firstSessionData = csvDataReader.parseIntoDataObject("/CSV_Data/session_1_Statistics.csv");
		this.secondSessionData = csvDataReader.parseIntoDataObject("/CSV_Data/session_2_Statistics.csv");		
	}	
}