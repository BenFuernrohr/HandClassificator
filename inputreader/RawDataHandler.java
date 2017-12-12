package inputreader;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to extract the relevant data from a session. 
 * Allows for the calculation of the avg handSpread-, thumbSpread, handRest- and thumbRest-Degrees
 * @author Ben Fürnrohr
 */
public class RawDataHandler{

	/** Raw HandData from a calibration session*/
	private List<HandData> dataFromCompleteRun;
	
	/** Extracted thumbSpread-fields from the raw HandData*/
	private List<Double> thumbSpreadData;
	
	/** Extracted handSpread-fields from the raw HandData*/
	private List<Double> handSpreadData;
	
	/** 
	 * Sets the raw data from the calibration session. 
	 * @param data List of raw, uninterpreted  {@link HandData}-objects
	 */
	public void setDataFromCompleteRun(List<HandData> data) {
		this.dataFromCompleteRun = data;
		List<Double> thumbDataList = new ArrayList<Double>();
		for(HandData hd: dataFromCompleteRun) {
			thumbDataList.add(hd.getThumb());
		}
		this.thumbSpreadData = thumbDataList;
		
		List<Double> handDataList = new ArrayList<Double>();
		for(HandData hd: dataFromCompleteRun) {
			handDataList.add(hd.getSpread());
		}
		this.handSpreadData = handDataList;
	}
	
	/**
	 * Calculates the average degree value representing a spread thumb
	 * @return degrees at which a thumb is expected to be spread
	 */
	public double calculateThumbSpreadAvg() {		
		double[] maxima = this.find3Maxima(this.thumbSpreadData);
		return (maxima[0]+maxima[1]+maxima[2])/3;		
	}
	
	/**
	 * Calculates the average degree value representing a spread hand
	 * @return hand at which a thumb is expected to be spread
	 */
	public double calculateHandSpreadAvg() {		
		double[] maxima = this.find3Maxima(this.handSpreadData);
		return (maxima[0]+maxima[1]+maxima[2])/3;
	}
	
	/**
	 * Calculates the average degree value representing a resting thumb
	 * @return degrees at which a thumb is expected to be rested
	 */
	public double calculateThumbRestAvg() {
		return this.avgFromData(this.thumbSpreadData);
	}
	
	/**
	 * Calculates the average degree value representing a resting hand
	 * @return degrees at which a hand is expected to be rested
	 */
	public double calculateHandRestAvg() {
		return this.avgFromData(this.handSpreadData);
	}
	
	/**
	 * Builds a {@link CalibrationDataset}-object from the relevant measures extracted from the calibration-setting
	 * 
	 * @return the new {@link CalibrationDataset}-object containing degrees for resting thumb, resting hand, spread thumb and spread hand 
	 */
	public CalibrationDataset generateCalibrationDataset() {
		return new CalibrationDataset(this.calculateThumbRestAvg(), this.calculateHandRestAvg(), this.calculateThumbSpreadAvg(), this.calculateHandSpreadAvg());		
	}
	
	/**
	 * Averages over a list of doubles
	 * @param data list of doubles
	 * @return the average of the values in the list
	 */
	private double avgFromData(List<Double> data) {
		double sum = 0;
		for (Double d : data) {
			sum = sum + d;
		}
		return sum/data.size();
	}

	/**
	 * Finds the 3 global maxima in a list of doubles
	 * @param dataList the list of doubles
	 * @return array of 3 highest points (highest, second, third)
	 */
	private double[] find3Maxima(List<Double> dataList) {
		
		//maxima in order
		double highest = 0;
		double second = 0;
		double third = 0;
		double current = 0.0;
		boolean rising = true;
		
		//find a maximum
		for(Double dataPoint : dataList) {		
			if (rising) {
				if (dataPoint < current) {
					//local Maximum found! Check for global
					rising = false;
					if (dataPoint > highest) {
						third = second;
						second = highest;
						highest = dataPoint;
					}
					else if (dataPoint > second) {
						third = second;
						second = dataPoint;
					}
					else if (dataPoint > third) {
						third = dataPoint;
					}
				}			
			}
			else {
				//value falling
				if (dataPoint > current) {
					//local Minimum found
					rising = true;
				}			
			}
			current = dataPoint;
		}
		double[] retArray = {highest, second, third};		
		return retArray;
	}	
}