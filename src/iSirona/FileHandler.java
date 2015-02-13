/**
 * 
 */
package iSirona;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * @authors Laura Jackson
 * 		 	Bryan Tanner
 * 
 * DataParser reads data from a textfile in hexadecimal
 * form and uses techniques described in the DINAMAP PRO 1000
 * Manual to translate that raw data into integer values that
 * can be mapped across time to show graphical output
 *
 */
public class FileHandler implements Iterable<String>{
	
	/**********************************\
	 * Attributes					  *
	\**********************************/
	
	// buffered reader and writer allows us to pull in large files using an 8k buffer
	private BufferedReader b_reader;
	private BufferedWriter b_writer;
	
	/**********************************\
	 * Methods						  *
	\**********************************/
	
	/**
	 * openReader creates an instance of the BufferedReader to
	 * read from the file passed into the method
	 * 
	 * @param filePath The path of the file to be read from
	 * @throws Exception General Exception thrown by the method
	 */	
	public void openReader(String filePath) throws Exception
	{
		b_reader = new BufferedReader(new FileReader(filePath));
	}
	
	/**
	 * openWriter creates an instance of the BufferedWriter
	 * to write to a text file
	 * 
	 * @param filePath The path of the file to be read from
	 * @throws Exception General Exception thrown by the method
	 */
	public void openWriter(String filePath) throws Exception
	{
		b_writer= new BufferedWriter(new FileWriter(filePath));
	}
	
	/**
	 * closeReader closes the file we are pulling data from
	 */
	public void closeReader()
	{
		try
		{
			b_reader.close();
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * closeWriter closes the file we are writing data to
	 */
	public void closeWriter()
	{
		try
		{
			b_writer.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * writeFile writes the array of shorts passed in as the 
	 * parameter to the output file in string form
	 * 
	 * @param content The array containing values to write to the file
	 * @throws IOException Input/Output exception
	 */
	public void writeFile(short[] content) throws IOException
	{
		// string to hold the value of the char to put into the file
		String val;
		// write the contents passed in to the output file
		try 
		{
			for (short i : content)
			{
				val = String.valueOf(i);
				b_writer.write(val);
				b_writer.newLine();
			}
		}
		catch(Exception ex) 
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * iterator returns an instance of an iterator used
	 * to read lines from the file
	 * 
	 * @return The instance of the Iterator that was created
	 */
	@Override 
	public Iterator<String> iterator()
	{
		return new FileIterator();
	}
	
	/**  
	 * private inner class implements the String iterator
	 * to iterate through the open file to read	 *
	 */
	private class FileIterator implements Iterator<String>
	{
		// String to keep track of the current line
		private String cur_line;
		
		/**
		 * hasNext ensures the iterator must always 
		 * have a next value to be called
		 */
		public boolean hasNext()
		{
			try
			{
				// try to read the next line of the file
				cur_line = b_reader.readLine();
			}
			catch (Exception ex)
			{
				cur_line = null;
				ex.printStackTrace();
			}
			
			// return whether there is a next object to read
			return cur_line != null;
		}
		
		/**
		 * next retrieves the next object in the file
		 * 
		 * @return The value read from the file returned as a string
		 */
		public String next()
		{
			return cur_line;
		}
		
		/**
		 * no action is necessary for this method because
		 * if the next item isn't retrieved using next then
		 * hasNext will skip the current line
		 */
		public void remove()
		{
		}			
	}
}
