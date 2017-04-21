package StringHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

public class StringFunction {
	
	public boolean checkIsUri(String str)
	{
		if(str.contains("http"))
		{
			return true;
		}
		
		return false;
	}

	public String checkObject(String tmp)
	{
		
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		
		StringTokenizer st;
		
		if(tmp.contains("@")){
			st = new StringTokenizer(tmp,"@"); 
			if(st.countTokens()>1)
		
			{
			
				while (st.hasMoreTokens()){ 
				
					tmp_str.add(st.nextToken()); 
				}
			return tmp_str.get(0);
		
			}
		
		}
		if(tmp.contains("^")){
			st = new StringTokenizer(tmp,"^"); 
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
			System.out.println("ddd");
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
	public ArrayList<String> checkVariableDuplication(String variable)
	{

		ArrayList<String> uriList=token(variable);
		ArrayList<String> tmpStrList=new ArrayList<String>();
		Iterator it;
		HashSet<String> hs = new HashSet<String>(uriList);
		
		it=hs.iterator();
		 while(it.hasNext())
		 {
			 tmpStrList.add(it.next().toString());
		 }
		 
		 return tmpStrList;
	}
	
	public ArrayList<String> checkUriDuplication(String uri)
	{

		ArrayList<String> uriSearchedList=token(uri);
		ArrayList<String> tmpStrList=new ArrayList<String>();
		Iterator it;
		HashSet<String> hs = new HashSet<String>(uriSearchedList);
		
		it=hs.iterator();
		 while(it.hasNext())
		 {
			 tmpStrList.add(it.next().toString());
		 }
		 
		 return tmpStrList;
	}
	
	
	
	public ArrayList<String> checkUriDuplication(ArrayList<String> uriList)
	{

		ArrayList<String> tmpStrList=new ArrayList<String>();
		Iterator it;
		HashSet<String> hs = new HashSet<String>(uriList);
		it=hs.iterator();
		 while(it.hasNext())
		 {
			 tmpStrList.add(it.next().toString());
		 }
		 
		 return tmpStrList;
	}
	
	public  ArrayList<String> token(String tmp)
	{
		ArrayList<String> tmp_str=new ArrayList<String>();
	
		String tmp2=null;
		String tmp3 = null;
		StringTokenizer st = new StringTokenizer(tmp," "); 
		while (st.hasMoreTokens()){ 
			tmp2=st.nextToken();
			if(tmp2.contains("?"))
			{
				if(tmp2.startsWith("?"))
				{
					if(tmp2.endsWith("}"))
					{
						tmp_str.add(token3(tmp2));
						continue;
					}
					
					if(tmp2.endsWith("."))
					{
						tmp_str.add(token4(tmp2));
						continue;
					}
					
					tmp_str.add(tmp2);
					continue;
				}
				
				if(tmp2.startsWith("{"))
				{
					tmp_str.add(token2(tmp2));
					continue;
				}
				
				
			}
		}
		
		for(int i=0; i<tmp_str.size(); i++)
		{
			System.out.println(tmp_str.get(i));
		}
		
		return tmp_str;

	}
	
	public String token2(String tmp)
	{
		StringTokenizer st = new StringTokenizer(tmp,"{"); 
		String tmp2=null;
		if(st.hasMoreTokens())
			tmp2=st.nextToken();
		return tmp2;
	}
	
	public String token3(String tmp)
	{
		StringTokenizer st = new StringTokenizer(tmp,"}"); 
		String tmp2=null;
		if(st.hasMoreTokens())
			tmp2=st.nextToken();
		return tmp2;
	}
	
	public String token4(String tmp)
	{
		StringTokenizer st = new StringTokenizer(tmp,"."); 
		String tmp2=null;
		if(st.hasMoreTokens())
			tmp2=st.nextToken();
		return tmp2;
	}

}
