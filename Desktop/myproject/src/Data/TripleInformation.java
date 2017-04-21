package Data;

import java.util.ArrayList;

public class TripleInformation {
	
	private static ArrayList<PODataList> list=new ArrayList<PODataList>();
	//private static ArrayList<Uri> initialUriList;
	private static ArrayList<String> sameAsList=new ArrayList<String>();
    
	public static ArrayList<PODataList> getList() {
		return list;
	}
	public static void setList(ArrayList<PODataList> list) {
		TripleInformation.list = list;
	}
	public static ArrayList<String> getSameAsList() {
		return sameAsList;
	}
	public static void setSameAsList(ArrayList<String> sameAsList) {
		TripleInformation.sameAsList = sameAsList;
	}
}
