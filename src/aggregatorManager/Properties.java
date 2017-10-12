package aggregatorManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * The Properties class saves the properties for the AM. We may have
 * more properties in the future so this class may be extended.
 * 
 * @author Konstantinos Dalianis
 * @author Maria Fava
 * @version 2.0
 * @since 2015-12-31
 */
public class Properties {
	/**
	 * Time of seconds to sleep between job requests
	 */
	public static int jobRequestInterval;

	/**
	 * Constructor of Properties. Saves all the properties read from the file in
	 * class variables.
	 * 
	 * @param propertyFile
	 *            Name of file including properties.
	 * @throws IOException
	 */
	public static void setProperties(String propertyFile) throws IOException {
		BufferedReader property = new BufferedReader(new FileReader(propertyFile));
		String line;
		while ((line = property.readLine()) != null) {
			String[] propertyInfo = line.split(" ");
			if (propertyInfo[0].equals("jobRequestInterval")) {
				jobRequestInterval = Integer.parseInt(propertyInfo[1]);
			}
		}
		property.close();
	}
}
