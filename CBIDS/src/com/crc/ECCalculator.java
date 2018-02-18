package com.crc;

import com.controller.PESController;
import com.data.Entity;
import com.lod.LOD;

public class ECCalculator {

	
	public double calculateEC(Entity source)
	{
		LOD lod=PESController.lodStorage.getLC(source);
		double ec=source.getEntityConfidence()*lod.getLodConfidence();//calculate EC
		
		return ec;
	}
}
