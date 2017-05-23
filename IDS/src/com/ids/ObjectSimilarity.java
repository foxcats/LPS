package com.ids;

import java.util.ArrayList;
import java.util.StringTokenizer;

import info.debatty.java.stringsimilarity.NGram;

public class ObjectSimilarity {

	public double calculateSimilarity(String sourceObject,String targetObject)
	 {
		NGram twogram=null;
		
		String srcObject;
		String trgObject;
		if(sourceObject.contains("http"))
		{
			srcObject=replaceS(sourceObject);
			trgObject=replaceS(targetObject);
			
		}else{
		
			srcObject=checkObject(checkObject(replaceS(sourceObject)));
		
			trgObject=checkObject(checkObject(replaceS(targetObject)));
		}
		
		
		
		if(srcObject.length()==1||trgObject.length()==1)
		{
			twogram=new NGram(1);
		}
		else{
			twogram = new NGram(2);
		}
		
        double similarity=twogram.distance(srcObject,trgObject);
        
       // System.out.println("소스:"+srcObject+" "+"타겟:"+trgObject+"  "+similarity);
        return similarity;
	 }

	 static String replaceS(String src){
	  String[] r = {" ","\t","-","\r","\n",".",","};
	  for (String t:r ) src = src.replace(t,"");
	  //System.out.println(" -> replaced src:"+src);
	  return src;
	 }
	 
	 
	 public static String checkObject(String tmp)
		{
			
			ArrayList<String> tmp_str=new ArrayList<String>();
		
			
			StringTokenizer st;
			
			if(tmp.contains("@")){
				//System.out.println("ddd");
				st = new StringTokenizer(tmp,"@"); 
				if(st.countTokens()>1)
			
				{
				
					while (st.hasMoreTokens()){ 
					
						tmp_str.add(st.nextToken()); 
					}
				return tmp_str.get(0);
			
				}
			
			}
			if(tmp.contains("("))
			{
				st = new StringTokenizer(tmp,"("); 
				if(st.countTokens()>1)
					
				{
				
					while (st.hasMoreTokens()){ 
					
						tmp_str.add(st.nextToken()); 
					}
				return tmp_str.get(0);
			
				}
			}
			
			if(tmp.contains("/"))
			{
				st = new StringTokenizer(tmp,"/"); 
				if(st.countTokens()>1)
					
				{
				
					while (st.hasMoreTokens()){ 
					
						tmp_str.add(st.nextToken()); 
					}
				return tmp_str.get(0);
			
				}

			}
			
			if(tmp.contains("["))
			{
				//System.out.println("ddd");
				st = new StringTokenizer(tmp,"["); 
				if(st.countTokens()>1)
					
				{
				
					while (st.hasMoreTokens()){ 
					
						tmp_str.add(st.nextToken()); 
					}
				return tmp_str.get(0);
			
				}

			}
			
			
			return tmp;

		}
}
