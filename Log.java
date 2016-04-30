import java.util.ArrayList;
//import java.util.Iterator;
import java.util.List;


public class Event {
	private String clock;
	private String userID;
	private String userComments;
	private String siteID = this.id;	
	List<List<String>> ls2d = new ArrayList<List<String>>(); 
	tt = new String [3][3];
	private bool isLocal (String cmpSiteID)) {
		if (cmpSiteID == siteID) {
			return True;
		}
		else return False;
	}
}

public class Log {
   List<List<String>> ls2d = new ArrayList<List<String>>(); 
 //  List<List<String>> tt = new ArrayList<List<String>>(); 
   tt = new long [3][3];
   private String clock;
   private String userID;
   private String userComments;
   private String siteID;


   public void handleEvent(Event event) {
      if (event.isLocal(this.siteID)) {    	  
        //  synchronized(object) {
    	  List<String> x = new ArrayList<String>();
          x.add(event.clock);
        //  x.add(event.siteID);
          x.add(event.userID);
          x.add(event.userComments);
          ls2d[siteID].add(x);
          tt.updateLocalTT(event, this);
      }
      else {   
    	  ls2d.insertLog(event.log);
    	  tt.combineRemoteTT(event);
    	  arr = tt.GCRemoteTT();
    	  ls2d.deleteLog(arr);	  
      }
       // JobUtil.processJob(event, this);
   }
   
   private void insertLog (Event event) {
	   for (int i = 0; i<3; i++) {
		   ls2d[i].add(event.log[i]);
	   }
   }

   private void deleteLog (long [] arr) {
	   for (int i=0; i<3; i++) {	   
	     for (int j=0; j< ls2d[i].size()/3; j=j+3) {
	       if (Integer.parseInt(ls2d[i][j]) <= arr[i]) {
		      ls2d[i].removeRange(j, j+2);
	       }
	     }
   }
   
   private long[] GCRemoteTT() {	   
       int x; // delete entry after x
       arr = new long[3];
       for (int j=0; j<3; j++) {
    	   int min = tt.[0][j];
    	   for (int i=0; i<3; i++) {
    		   min = min(min, tt[i][j]);
    	   }
    	   arr[j] = min;
    	 //  ls2d.deleteEntry(j, min); // site j to clk min
       }	
       return arr;
   }
   
   private void combineRemoteTT (Event event) {
	   int x = Integer.parseInt(event.siteID);
	   for (int i=0; i<3; i++) {
		   for (int j=0; j<3; j++) {
			   this.tt[i][j] = max(event.tt[i][j], this.tt[i][j]);
			   if (i==x) {
				   this.tt[siteID][j] = max(this.tt[siteID][j], event.tt[i][j]);
			   }
		   }		
	   }
   }
   
   private void updateLocalTT(Event event) {
	   int x = Integer.parseInt(event.siteID);
	   tt[x][x] = event.clock;
   }

 }
   





/* public class TimeTable {

for(List<String> ls : ls2d) {
   System.out.println(Arrays.deepToString(ls.toArray()));
}
} */

/*public class TestArray {

   public static void main(String[] args) {
      double[] myList = {1.9, 2.9, 3.4, 3.5};

      // Print all the array elements
      for (int i = 0; i < myList.length; i++) {
         System.out.println(myList[i] + " ");
      }
      // Summing all elements
      double total = 0;
      for (int i = 0; i < myList.length; i++) {
         total += myList[i];
      }
      System.out.println("Total is " + total);
      // Finding the largest element
      double max = myList[0];
      for (int i = 1; i < myList.length; i++) {
         if (myList[i] > max) max = myList[i];
      }
      System.out.println("Max is " + max);
   }
} */