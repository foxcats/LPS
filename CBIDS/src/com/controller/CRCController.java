package com.controller;

import com.crc.ECCalculator;
import com.crc.ECRenewal;
import com.data.Entity;
import com.lod.LOD;

public class CRCController {

	
	/**
	 * calculate EC and propagate EC
	 * @param source
	 * @param target
	 * @return
	 */

	
	public boolean confidenceCleansing(Entity source,Entity target)
	{
		ECCalculator calculator=new ECCalculator();
		ECRenewal renewal=new ECRenewal(calculator);
		
		
		double ec=calculator.calculateEC(source);
		
		if(ec>1)
		{
			ec=1;
		}
		
		
		target.setEdgeCount(target.getEdgeCount()+1);
		
		if(target.getVisitFlag()==1)	
		{
		
			PESController.agreement++;
			System.out.println("동조발생! : "+	PESController.agreement);
			renewal.renewEC(source,target);
			return  false;
		}
		else
		{
			if(ec>target.getEntityConfidence()){
				
				target.setEntityConfidence(ec);
				target.getDc().insertEC(source.getDepth()-1, ec, target.getEdgeCount());
			}
			
		}
		
		return true;
	}
	
	public boolean confidenceCleansing2(Entity source,Entity target)
	{
		//ECCalculator calculator=new ECCalculator();
		//ECRenewal renewal=new ECRenewal(calculator);
		
		
		//double ec=calculator.calculateEC(source);
		/*
		if(ec>1)
		{
			ec=1;
		}
		*/
		
		//target.setEdgeCount(target.getEdgeCount()+1);
		
		if(target.getVisitFlag()==1)	
		{
		
			PESController.agreement++;
			//System.out.println("동조발생! : "+	PESController.agreement);
			//renewal.renewEC(source,target);
			return  false;
		}
		
		
		return true;
	}

	
	
}
