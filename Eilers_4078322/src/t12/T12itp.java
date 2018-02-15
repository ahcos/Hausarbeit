package t12;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import t12.spinphone.T12Interpreter;
import t12.util.KeyConverter;


public class T12itp implements T12Interpreter {

	private Lexicon lex;
	private T9Node current;
	private String currentWordAsNumber = "";
	private int leafsNumber = 0;
	private boolean upperCase = false;
	private boolean allUpperCase = false;
	private boolean numbersEntryMode = false;
	private boolean wordCompleted = false;
	
	
	
	@Override
	public String buttonPressed(int number) {
		
		if (wordCompleted) {
			this.current = lex.getRoot();
			this.leafsNumber = 0;
			this.currentWordAsNumber = "";
			wordCompleted = false;
		}
		
		if (numbersEntryMode)
			return Integer.toString(number);
		
		
		// if currentWord is empty, begin new word
		else if (currentWordAsNumber.length() == 0) 
			current = lex.getRoot();
		
		number = number - 2; // to match the Childnode Array-Number in the T9Node-Objekt
		String toReturn = "";
		currentWordAsNumber = currentWordAsNumber + Integer.toString(number); // save word as String of Numbers
		
		
		try {
			if (current.getChildNode(number) == null)
				return KeyConverter.convertToKey(number);
				
			else {
				if (current.getChildNode(number).getLeafs() == null) {
					current = current.getChildNode(number);
					return KeyConverter.convertToKey(number);
				}
				else {
					toReturn = current.getChildNode(number).getLeafs().get(0).getKey();
					current = current.getChildNode(number);
				}
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Asterisk pressed -> first Char toUpperCase
		if (upperCase && toReturn.length() > 0) {
			String replacementForFirstCharacter = toReturn.substring(0, 1).toUpperCase();
			toReturn = toReturn.substring(1, toReturn.length());
			toReturn = replacementForFirstCharacter + toReturn;
		}
		if (allUpperCase && toReturn.length() > 0) {
			toReturn = toReturn.toUpperCase();
		}
		
		
		return toReturn;
	}
	public Lexicon getLex() {
		return lex;
	}

	@Override
	public void generateLexicon(String pathToTexts, String lexFileDestination) {
		Lexicon lex = new Lexicon(pathToTexts);
		lex.generateLexicon();
		this.lex = lex;
		
		try {
		
//			Gson gson = new Gson();
//			String json = gson.toJson(lex);
//			PrintWriter out = new PrintWriter(lexFileDestination);
//			out.println(json);
//			out.close();
			FileOutputStream fs = new FileOutputStream(lexFileDestination);
			OutputStream buffer = new BufferedOutputStream(fs);
			ObjectOutputStream os = new ObjectOutputStream(buffer);
			os.writeObject(lex);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	@Override
	public void loadLexicon(String lexFilePath) {
		try {
			InputStream file = new FileInputStream(lexFilePath);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			this.lex = (Lexicon) input;
			input.close();
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public String getAlternative() {
		
		WordAlternative alt = new WordAlternative(leafsNumber);
		String toReturn = alt.findAlternative(current);	
		this.leafsNumber = alt.getLeafsNumber();
		return toReturn;
		
	}

	@Override
	public void learn(String newWord) {
		
		HashMap<String, Integer> in = new HashMap<>();
		in.put(newWord, 1);
		
		lex.learnWord(lex.getRoot(), in);

	}

	@Override
	public void asteriskButtonPressed() {
		if (upperCase) {
			upperCase = false;
			allUpperCase = true;
			System.out.println("All Charcters Upper Case!");
		}
		else if (allUpperCase) {
			allUpperCase = false;
			System.out.println("No Character Upper Case!");
		}
		else {
			upperCase = true;
			System.out.println("First Charcter Of Each Word Upper Case!");
		}

	}

	@Override
	public void numberSignButtonPressed() {
		if (numbersEntryMode) {
			numbersEntryMode = false;
			System.out.println("Numbers Entry Mode Off.");
		}
		else {
			numbersEntryMode = true;
			current = lex.getRoot();
			currentWordAsNumber = "";
			leafsNumber = 0;
			System.out.println("Numbers Entry Mode On! Press Again To Switch Off.");
		}
	}

	@Override
	public String delButtonPressed() {
		// 1. set node to it's parent node 2. shorten currentWordAsNumber by one 3. leafsnumber = 0 4. search for String with "new" Input
		if (current.getParentNode() == null) {
			current = lex.getRoot();
			return "";
		}
		current = current.getParentNode();
		currentWordAsNumber = currentWordAsNumber.substring(0, currentWordAsNumber.length()-1);
		leafsNumber = 0;
		// last Number of currentWordAsNumber
		int number = Integer.parseInt(currentWordAsNumber.substring(currentWordAsNumber.length()-1,currentWordAsNumber.length()));
		
		if (current.getChildNode(number).getLeafs() == null) {
			return "";
		}
		else {
			return current.getChildNode(number).getLeafs().get(0).getKey();
		}
	}

	@Override
	public void wordCompleted() {
		wordCompleted = true;
		
	}

	@Override
	public String getAuthorName() {
		return "Frank Marian Eilers";
	}
	
}
