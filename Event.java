import java.util.ArrayList;
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