package main;

import java.util.List;

import classification.Classification_Result;
import classification.Classifyable;
import classification.HandSpreadCount_Classificator;
import classification.MovementCorrection_Classificator;
import classification.MultipleThumbSpread_Classificator;
import classification.ZAxisVariation_Classificator;
import dempster.DempsterHandler;
import evaluation.DempsterEvaluator;
import evaluation.NeticaEvaluator;
import inputreader.CalibrationDataset;
import inputreader.CsvDataReader;
import inputreader.HandData;
import inputreader.RawDataHandler;

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
	
	
	
	public static void main(String args[]) {		
		
		MainClass mainClass = new MainClass();
		mainClass.createCalibrationDataset();
		mainClass.extractSessionData();	
		mainClass.initializeClassificators();
		DempsterEvaluator dempsterEvaluator = new DempsterEvaluator();	
		NeticaEvaluator neticaEvaluator = new NeticaEvaluator();
				
		//evaluate first Session
		System.out.println("Evaluating first Session:");
		mainClass.setSessionDataForClassificators(mainClass.firstSessionData);
		
		Classification_Result handSpreadClassification = mainClass.handSpreadCount.classify();
		Classification_Result movementCorrectionClassification = mainClass.movementCorrection.classify();
		Classification_Result multipleThumbSpreadClassification = mainClass.multipleThumbSpread.classify();
		Classification_Result zAxisVariationClassification = mainClass.zAxisVariation.classify();
					
		DempsterHandler dempsterHandler1 = new DempsterHandler(3);
		
		dempsterEvaluator.evaluateClassification(dempsterHandler1, zAxisVariationClassification, movementCorrectionClassification, multipleThumbSpreadClassification, handSpreadClassification);
		dempsterEvaluator.interpretMeasure(dempsterHandler1.getFirstMeasure());
		
		neticaEvaluator.evaluateClassification(zAxisVariationClassification, movementCorrectionClassification, multipleThumbSpreadClassification, handSpreadClassification);
		
		
		
		//evaluate second Session
		System.out.println("Evaluating second Session:");
		mainClass.setSessionDataForClassificators(mainClass.secondSessionData);
		Classification_Result handSpreadClassification2 = mainClass.handSpreadCount.classify();
		Classification_Result movementCorrectionClassification2 = mainClass.movementCorrection.classify();
		Classification_Result multipleThumbSpreadClassification2 = mainClass.multipleThumbSpread.classify();
		Classification_Result zAxisVariationClassification2 = mainClass.zAxisVariation.classify();
		
		DempsterHandler dempsterHandler2 = new DempsterHandler(3);
		
		dempsterEvaluator.evaluateClassification(dempsterHandler2, zAxisVariationClassification2, movementCorrectionClassification2, multipleThumbSpreadClassification2, handSpreadClassification2);
		dempsterEvaluator.interpretMeasure(dempsterHandler2.getFirstMeasure());
		
		neticaEvaluator.evaluateClassification(zAxisVariationClassification2, movementCorrectionClassification2, multipleThumbSpreadClassification2, handSpreadClassification2);
	}
	
	private void initializeClassificators() {
		handSpreadCount = new HandSpreadCount_Classificator(calibrationDataSet);
		movementCorrection = new MovementCorrection_Classificator(calibrationDataSet);
		multipleThumbSpread = new MultipleThumbSpread_Classificator(calibrationDataSet);
		zAxisVariation = new ZAxisVariation_Classificator(calibrationDataSet);
		
	}

	private void setSessionDataForClassificators(List<HandData> sessionData) {
		handSpreadCount.setSessionData(sessionData);
		movementCorrection.setSessionData(sessionData);
		multipleThumbSpread.setSessionData(sessionData);
		zAxisVariation.setSessionData(sessionData);
		
	}

	private void createCalibrationDataset() {
		List<HandData> extractedData = csvDataReader.parseIntoDataObject("/CSV_Data/sample_0_Statistics.csv");

		RawDataHandler rawDataHandler = new RawDataHandler();
		rawDataHandler.setDataFromCompleteRun(extractedData);
		
		double thumbSpreadAvg = rawDataHandler.thumbSpreadAvg();
		double handSpreadAvg = rawDataHandler.handSpreadAvg();
		double thumbRestAvg = rawDataHandler.thumbRestAvg();
		double handRestAvg = rawDataHandler.handRestAvg();
		
		this.calibrationDataSet = new CalibrationDataset(thumbRestAvg, handRestAvg, thumbSpreadAvg, handSpreadAvg);		
	}
	
	private void extractSessionData() {
		this.firstSessionData = csvDataReader.parseIntoDataObject("/CSV_Data/session_1_Statistics.csv");
		this.secondSessionData = csvDataReader.parseIntoDataObject("/CSV_Data/session_2_Statistics.csv");		
	}
	
		
}