import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

ArrayList<String> arr = new ArrayList<String>();
arr.add("trinity");
Iterator<String> foreach = arr.iterator();
while (foreach.hasNext()) System.out.println(foreach.next());



public class Event {
   List<List<String>> ls2d = new ArrayList<List<String>>(); 
   List<List<String>> tt = new ArrayList<List<String>>(); 
   private string clock;
   private string userID;
   private string userComments;

   /*if (localEventUpdate) {
	  synchronized(object) {
	   List<String> x = new ArrayList<String>();
       x.add(clock);
       x.add(userID);
       x.add(userComments);
       ls2d.add(x);
	  }
   }
   else if () {
	   
   }
   for(List<String> ls : ls2d) {
      System.out.println(Arrays.deepToString(ls.toArray()));
   }
} */

   public void handleEvent(Event event) {
      if (event.isLocal()) {
    	  
        //  synchronized(object) {
    	  List<String> x = new ArrayList<String>();
          x.add(event.clock);
          x.add(event.userID);
          x.add(event.userComments);
          ls2d.add(x);
          tt.updateLocal(clock);
        //  }
      }
      else if (event.isRemote())) { 	   
    	  tt.combineRemote(event);
    	  ls2d.updateRemote(event);
      }
       // JobUtil.processJob(event, this);
   }
   
   private void updateRemote(Event event) {	   
       int x; // delete entry after x
       x = ls2d.compareClock(event.ls2d); // examine tt column to check if gc needed
	   ls2d.delete(x);	   
   }
   
   private void combineRemote(Event event) {
	   event.tt.compare();
   }
   
   
   
   private boolean shouldReplicate(final Resource resource, Event event) {
       if(StringUtils.equals(event.getTopic(), SlingConstants.TOPIC_RESOURCE_CHANGED)) {
           ValueMap properties = resource.adaptTo(ValueMap.class);

           final Date lastModified = properties.get(JcrConstants.JCR_LASTMODIFIED, Date.class);
           final Date lastReplicated = properties.get("cq:lastReplicated", Date.class);
           
           if(lastReplicated == null) { return true; }
           if(lastModified == null) { setLastModified(resource); }
           
           log.debug("LM " + lastModified.getTime()  + " >= LR " + lastReplicated.getTime() + " => " + lastModified.after(lastReplicated));
           
           // Last Modified must be >= Last Replicated
           return lastModified.after(lastReplicated);
       } else {
           return true;
       }
   }
   

public class TimeTable {
	   List<List<String>> ls2d = new ArrayList<List<String>>();  
	   if (eventUpdate) {
		  List<String> x = new ArrayList<String>();
	      x.add(clock);
	      x.add(userID);
	      x.add(userComments);
	      ls2d.add(x);
	   }
	   for(List<String> ls : ls2d) {
	      System.out.println(Arrays.deepToString(ls.toArray()));
	   }
	}



public class TestArray {

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
}