package com.calculator;

import java.util.Enumeration;
import java.util.Hashtable;

import com.lod.LOD;

public class Nomalization {
	/**
	 * Nomalization for lOD Confidence
	 * 
	 * LOD confidence has range of values 0~1
	 * @param lodList
	 */
	
	
	public void calculateNomalization(Hashtable<Integer,LOD> lodList)
	{
		double allLodConfidence=0;
		
		for(int i=0; i<lodList.size(); i++)
		{
			allLodConfidence+=lodList.get(i).getLodConfidence();
		}
		
		double average=calculateAverage(lodList.size(),allLodConfidence);
		double stdev=calculateStdev(lodList,average);
		
		for(int i=0; i<lodList.size(); i++)
		{
			double nomalization=(lodList.get(i).getLodConfidence()-average)/stdev;
			lodList.get(i).setLodConfidence(nomalization);
		}
	}
	
	public double calculateAverage(int num,double allLodConfidence)
	{
		double average=0;
		
		
		average=allLodConfidence/num;
		
		return average;
	}

	
	public double calculateStdev(Hashtable<Integer,LOD> lodList,double average)
	{
		double variance=0;
		double stdev=0;
		
		double num=0;
		double difference=0;
		for(int i=0; i<lodList.size(); i++)
		{
			num=lodList.get(i).getLodConfidence();
			difference=num-average;
			variance+=(difference*difference);
		}
		stdev=Math.sqrt(variance/lodList.size());
		return stdev;
	}
	
	
	public void standardization(Hashtable<Integer,LOD> lodList)
	{
		double min=0;
		double max=0;
		
		for(int i=0; i<lodList.size(); i++)
		{
			if(i==0)
			{
				min=lodList.get(i).getLodConfidence();
				max=lodList.get(i).getLodConfidence();
				continue;
			}
			
			if(lodList.get(i).getLodConfidence()<min)
			{
				min=lodList.get(i).getLodConfidence();
			}
			else if(lodList.get(i).getLodConfidence()>max)
			{
				max=lodList.get(i).getLodConfidence();
			}
			else
			{
				continue;
			}
		}
		
		
		for(int i=0; i<lodList.size(); i++)
		{
			double stadiz=(lodList.get(i).getLodConfidence()-min)/(max-min);
			lodList.get(i).setLodConfidence((stadiz*0.3)+0.7);
		}
	}
	
	
	public void standardization2(Hashtable<String,LOD> lodList)
	{
		double min=0;
		double max=0;
		Enumeration e=lodList.elements();
		int i=0;
		
		while(e.hasMoreElements())
		{
			LOD tmp=(LOD)e.nextElement();
			
			if(i==0)
			{
				min=tmp.getLodN();
				max=tmp.getLodN();
				i++;
				continue;
			}
			
			if(tmp.getLodN()<min)
			{
				min=tmp.getLodN();
			}
			else if(tmp.getLodN()>max)
			{
				max=tmp.getLodN();
			}
			else
			{
				i++;
				continue;
			}
			i++;
		}
		 e=lodList.elements();
		 
		 while(e.hasMoreElements())
		 {
			 LOD tmp=(LOD)e.nextElement();
			 double stadiz=(tmp.getLodN()-min)/(max-min);
			 tmp.setLodConfidence((stadiz*0.3)+0.4);
		 }
	}
}
