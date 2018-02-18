package com.lod;

import java.util.Hashtable;

public class LODCloud {
	
	Hashtable<String,LOD> lodCloud;
	
	
	

	public LODCloud() {
		this.lodCloud =new Hashtable<String,LOD>();
	}

	public Hashtable<String,LOD> getLodCloud() {
		return lodCloud;
	}

	public void setLodCloud(Hashtable<String,LOD> lodCloud) {
		this.lodCloud = lodCloud;
	}


	public void initAllEntityConf()
	{
		for(int i=0; i<100; i++)
		{
			LOD lod=this.lodCloud.get(i);
			lod.setLodConfidence(0);
		}
	}
}
