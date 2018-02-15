package t12;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import t12.util.KeyConverter;

public class Lexicon implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2940636273361812722L;
	private T9Node root;
	private String dataPath;
	
	public Lexicon (String dataPath) {
		this.dataPath = dataPath;
	}
	
	public T9Node getRoot () {
		return root;
	}
	
	public void generateLexicon() {
		
		DataProcessing data = new DataProcessing(dataPath);		
		HashMap<String, Integer> wordMap = data.processData();
	
		T9Node root = new T9Node(null);
		
		this.root = generateTree(root, wordMap);
	
	
	}
	
	public void learnWord (T9Node root, HashMap<String, Integer> wordMap) {
		
		this.root = generateTree(root, wordMap);
		
	}
	
	private T9Node generateTree(T9Node root, HashMap<String, Integer> wordMap) {
		

		// every word of the map
		for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
			
			
			char[] chars = entry.getKey().toCharArray(); // split word to chars
			Integer[] wordAsInt = new Integer[chars.length]; // int array to hold value of chars to integer
						
			for (int i = 0 ; i < chars.length; i++) {
				wordAsInt[i] = KeyConverter.convertToNumber(chars[i]);
			}
			// for every single number in the array - "hello" -> 	4 3 5 5 6 - create Node(s) if needed /count up Node Occurence(s) / Add Word as "leafs"-entry to final Node
			//										ArrayPosition:	0 1 2 3 4
			T9Node current = root;
			
			try {
				for (int i = 0; i < wordAsInt.length; i++) {
				
					
					int pos = wordAsInt[i] - 2; // position of the childnode in the T9Node-Object "current" childnode-array
					
					// start + no childnode yet at position 
					if (i == 0 && root.getChildNode(pos) == null) {
						T9Node childNode = new T9Node (root);
						root.setChildNode(pos, childNode);
						childNode.addNodeOccurence(); // block that inserts Node counts up occurence!
						current = childNode;
						
					}

					else {
						// if end of wordAsInt is reached, this node shall hold the entry as leaf-value
						if (current.getChildNode(pos) == null && !(i == wordAsInt.length - 1)) { //wordend not reached, childnode doesnt exist
						
							T9Node childNode = new T9Node(current);
							current.setChildNode(pos, childNode);
							childNode.addNodeOccurence();
							current = childNode;
						}
						else if (current.getChildNode(pos) == null && i == wordAsInt.length -1 ) { //wordend reached, childnode doesnt exist
					
							T9Node childNode = new T9Node(current);
							current.setChildNode(pos, childNode);
							childNode.addNodeOccurence();
							childNode.setLeafEntry(entry);
							
						}
						else if (current.getChildNode(pos) != null && i == wordAsInt.length -1) {// wordend reached, childnode exists
						
							current.getChildNode(pos).setLeafEntry(entry);
							current.getChildNode(pos).addNodeOccurence();
						}
						else {																	// wordend not reached, childnode exists
							
							current.getChildNode(pos).addNodeOccurence();
							current = current.getChildNode(pos);
							
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			
		}
		
		return root;
		
	}
	

}
