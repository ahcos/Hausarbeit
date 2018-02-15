package t12;

import java.io.Serializable;
import java.util.ArrayList;

public class WordAlternative implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5124260143624084702L;
	private int leafsNumber;
	
	public WordAlternative (int leafsNumber) {
		this.leafsNumber = leafsNumber;
	}
	public int getLeafsNumber () {
		return leafsNumber;
	}
	
	public String findAlternative (T9Node current) {
				
		return findAlternative(current, leafsNumber);				
	}
	
	private String findAlternative (T9Node current, int pos) {
	
		try {
			// if no leaf entry in current node, check one level deeper in the tree for most likely alternative
			if (current.getLeafs() == null) {
				ArrayList <T9Node> listOfChildNodes = new ArrayList<>();
				//all Childnode-elements into list
				for (T9Node elem : current.getChildNodeArray()) {
					if (elem != null){
						listOfChildNodes.add(elem);
					}
				}
				// no childnodes, no alternative
				if (listOfChildNodes.size() == 0)
					return "No Such Word!";
				else {
					// get most likely childenode by sorting by occurence
					listOfChildNodes.sort((T9Node o1, T9Node o2) -> (o2.getNodeOccurence() - o1.getNodeOccurence()));
					
					//recursive until end of tree is reached, or alternative is found
					current = listOfChildNodes.get(0);
					return findAlternative(current, 0);
				}
			
			}
			// if there is a current leaf-entry
			else {
				if (current.getLeafs().get(leafsNumber +1) != null) {
					
					this.leafsNumber++;
					return current.getLeafs().get(leafsNumber).getKey();
				}
				else {
					ArrayList <T9Node> listOfChildNodes = new ArrayList<>();
					//all Childnode-elements into list
					for (T9Node elem : current.getChildNodeArray()) {
						if (elem != null){
							listOfChildNodes.add(elem);
						}
					}
					// no childnodes, no alternative
					if (listOfChildNodes.size() == 0)
						return "No Such Word!";
					else {
						// get most likely childenode by sorting by occurence
						listOfChildNodes.sort((T9Node o1, T9Node o2) -> (o2.getNodeOccurence() - o1.getNodeOccurence()));
						
						current = listOfChildNodes.get(0);
						//recursive until end of tree is reached, or alternative is found
						return findAlternative(current, 0);
					}
				}
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();			
		}
		return null;
		
		
		
	}

}
