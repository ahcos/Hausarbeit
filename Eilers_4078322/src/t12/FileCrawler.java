package t12;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class FileCrawler implements FileVisitor<Path>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6579381764933101918L;
	private String fileContents ="";

	public String getFileContents() {
		return fileContents;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path toFile, BasicFileAttributes attrs) throws IOException {
		StringBuilder stringbuilder = new StringBuilder();
        
		 
        try (Stream<String> stream = Files.lines( Paths.get(toFile.toString()), StandardCharsets.UTF_8)) 
        {
       
        	
            stream.forEach(s -> stringbuilder
            		.append(s)
            		.append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String temp = stringbuilder.toString();
        
        
       fileContents = fileContents + temp;
		
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
		// TODO Auto-generated method stub
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {

		return FileVisitResult.CONTINUE;
	}

}
