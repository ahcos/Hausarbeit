package t12;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

/**
 * Used for processing Data specified by the given File or Directory. Gets all the Data using FileCrawler, cuts all unnecessary parts of the text, and returns as Map<String, Integer>.
 * @author FME
 * @see FileCrawler
 */


public class DataProcessing implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6586482235379590011L;
	private String pathToFile;
	
	public DataProcessing(String pathToFile) {
		
		this.pathToFile = pathToFile;
	}
	
	/** Bearbeitet die Daten so, dass sie für den T9-Datenobjekt-Baum brauchbar werden
	 * 
	 * @return HashMap<String, Integer> mit den Wörtern der Dateien als "key" und ihrem Vorkommen als "value"
	 * @see FileCrawler
	 */
	public HashMap<String, Integer> processData() {
	FileCrawler f = new FileCrawler(); // Klasse
	File file = new File (pathToFile); // temp file zum erzeugen des absolutePaths
	Path path = Paths.get(file.getAbsolutePath()); // eigentlicher Pfad
		
	try {
			Files.walkFileTree(path, f); // erzeugtes FileCrawler Objekt wird ausgeführt
	} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}	
	
	String temp = f.getFileContents(); //das Feld des FileCrawler Objekts wird ausgelesen
	
	temp = temp.toLowerCase();
	
    temp = temp.replaceAll("[^a-z0-9äöüß]", " "); // alle Zeichen die nicht wie angegeben sind durch " " ersetzen
    temp = temp.replaceAll("\\w*([\\wäuöß])\\1{2,}\\w*", ""); // alle Wörter entfernen, die einen Buchstaben mehr als 2 mal enthalten

	
	String[] s = temp.replaceAll("\\w*\\d\\w*", "").trim().split(" +"); //alle Wörter mit Zahlen entfernen sowie Zahlen allgemein; trennen an einer oder mehr Leerzeichen

	HashMap<String, Integer> words = new HashMap<>();
	
	// in die HashMap schreiben
	
	for (String string : s) {
		if (!words.containsKey(string))
			words.put(string, 1);
		else {
			words.put(string, words.get(string) + 1); 
		}
	}
	
	//temporäre HashMap um aus der Original-Hashmap alle Wörter zu löschen, die nur 1x vorkommen
	
	HashMap<String, Integer> clone = new HashMap<String, Integer>(words);
	
	for (HashMap.Entry<String, Integer> entry : clone.entrySet()) {
		if (entry.getValue() == 1 )
			words.remove(entry.getKey());
	}
	
	// HashMap sortieren 
	
	words = sortByValue(words);
	
	return words;
	
	}
	/**
	 * Sortiert die HashMap absteigen - für aufsteigend Collections.reverseOrder() auskommentieren
	 * @param HashMap<String, Integer>
	 * @return sortiert absteigend
	 */
	
	private HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(HashMap.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                HashMap.Entry::getKey, 
	                HashMap.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
	


}
