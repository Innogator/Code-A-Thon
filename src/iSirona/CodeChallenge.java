/**
 * 
 */
package iSirona;

import javax.swing.JFrame;

/**
 * @authors Laura Jackson
 * 			Bryan Tanner
 * 
 * CodeChallenge demonstrates the parsing and graphical
 * analysis of data returned from a DINAMAP PRO 1000 monitor
 * used in Native Binary Mode. 
 */

/*		>>>---;;;-->		>>>---;;;-->
 * 				>>>---;;;-->		>>>---;;;-->
 *						>>>---;;;-->	>>>---;;;-->
 *	____________	 ____________	____	   ____
 *	|			|	/	 _______|	|	| 	   |   |
 *	|	________|	|	|_______	|	|	   |   |
 *	|	|			\______		\	|   |      |   |
 *	|	|____				\	|	|	|	   |   |
 *	|	 ____|		________|	|	\	\______/   /
 *	|	|			|			|	 \			  /
 *	|___|			|___________/	  \__________/
 *\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//
 */

public class CodeChallenge 
{	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{		
		// read the data, parse it, and put it in output file
		readData();
	}
	
	private static void readData() 
	{
		/**				!!!		!!!		!!!		!!!		!!!		!!!
		 * PLEASE CHANGE THE VARIABLE "inFilePath" TO THE PATH OF THE FILE TO READ FROM
		 * AND CHANGE THE VARIABLE "outFilePath" TO THE PATH OF THE FILE TO WRITE TO
		 */
		// declare the file paths
		String inFilePath = "C:\\Users\\Bryan\\Desktop\\FSU Fall-2012\\iSirona_Code-a-Thon\\Dinamap-Data.txt";
		String outFilePath = "C:\\Users\\Bryan\\Desktop\\FSU Fall-2012\\iSirona_Code-a-Thon\\Parsed-Data.txt";
		// declate a FileHandler to handle input and output
		FileHandler handler = new FileHandler();
		// the divisor is used to convert to milliamps
		final int divisor = 1000;
		// counter keeps track of how many lines of code are in a file so arrays
		// can be assigned matching lengths
		int counter = 0;
		
		try
		{
			// pass the file paths to the reader and writer
			handler.openReader(inFilePath);
			handler.openWriter(outFilePath);
			// array of short values representing wave form values
			short[] wf_data;			
	 
			// iterate through each line of code, parsing each block and
			// writing the parsed data to a separate output file
			for (Object line : handler)
			{
				// send the data to the parser class to transform the data to usable code
				wf_data = DataParser.parseString(line.toString());
			
				// write the data to the text file to be used in graphing
				handler.writeFile(wf_data);
				counter++;
			}
		
			// close the file handlers files
			handler.closeReader();
			handler.closeWriter();
			
			// since there are 4 plot points per block, we multiple number
			// of lines of code by 4 to get the number of plot points
			double[] wf_x_coordinates = new double[counter * 4];
			double[] wf_y_coordinates = new double[counter * 4];
		
			// now open the file we just wrote to read the values to be graphically displayed
			handler.openReader(outFilePath);
			int i = 0;
			double converted;
			for (String val : handler)
			{
				// convert the data to a double and divide
				// by 1000 to get the correct unit of measurement.
				// If the value is outside of the normal range then
				// divide by two to eliminate spikes
				converted = (Double.parseDouble(val));
				wf_y_coordinates[i] = converted/divisor;
				if (wf_y_coordinates[i] > 1)
					wf_y_coordinates[i] /= 2;
				wf_x_coordinates[i] = i * 5;
				i++;
			}
			handler.closeReader();
		
			// call the graphing function to plot the points		
			PlotGraph test = new PlotGraph();  
			test.setData(wf_x_coordinates, wf_y_coordinates);
			JFrame f = new JFrame();
			f.add(test.getContent());        
			f.add(test.getButtonPanel(), "Last");        
			f.setSize(1200, 800);        
			f.setLocation(50, 50);        
			f.setVisible(true);     		
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
