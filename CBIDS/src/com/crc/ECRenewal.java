package com.crc;

import com.controller.PESController;
import com.data.Entity;

public class ECRenewal {
	
	private ECCalculator ecCalculator;
	
	
	public ECRenewal(ECCalculator ecCalculator)
	{
	
		this.ecCalculator=ecCalculator;
	}


	
	
	public void renewEC(Entity agreementSourceEntity, Entity agreementTargetEntity)
	{
		/**
		 * cycleOccurPoint is entity that point target entity that has visited
		 */
	
		Entity next=null;
		Entity current=agreementTargetEntity;
		double confPoint=this.ecCalculator.calculateEC(agreementSourceEntity)+agreementTargetEntity.getEntityConfidence();
		
		
		while(true)//tour entities that participate in cycle
		{
			if(current.getEntityConfidence()>=confPoint)
			{
				return;
			}
			else{
				
				if(confPoint>1)//if EC is 1, not renewal
				{
					confPoint=1;
				}
				current.setEntityConfidence(confPoint);
				current.getDc().insertEC(current.getDepth(), confPoint, current.getEdgeCount());
			}
		
			next=current.getSameAsList().get(current.getIndex()-1);
			
			if(agreementTargetEntity==next)//
			{
				break;
			}
			
			confPoint=this.ecCalculator.calculateEC(current);
			current=next;
		}
		
		return;
	}
}
