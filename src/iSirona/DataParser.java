/**
 * 
 */
package iSirona;

/**
 * @authors Laura Jackson
 * 			Bryan Tanner
 * 
 * DataParser reads data from a textfile in hexadecimal
 * form and uses techniques described in the DINAMAP PRO 1000
 * Manual to translate that raw data into integer values that
 * can be mapped across time to show graphical output
 */
public class DataParser {
	
	/**********************************\
	 * Attributes					  *
	\**********************************/
	
	//*//*//*//*//*//*//*//*//*//*//*//*//
	
	/**********************************\
	 * Methods						  *
	\**********************************/
	// I just put it this way to test it, I will change it to return an array of shorts and take a String as argument
    public static short[] parseString (String code)
    {	
    	// declare a char array to hold the characters containing the packed data
    	char pwda[] = new char[5];
    	// declare an array to hold the parsed data
    	short wfs[] = new short[4];
    	// declare a string to hold the value to be parsed
    	String str = code;
        
    	// pull the data from the code black at the appropriate indexes
    	// while converting that data to a char
        pwda[0] = (char) (Short.parseShort(str.substring(14, 16), 16));
        pwda[1] = (char) (Short.parseShort(str.substring(16, 18), 16));
        pwda[2] = (char) (Short.parseShort(str.substring(18, 20), 16));
        pwda[3] = (char) (Short.parseShort(str.substring(20, 22), 16));
        pwda[4] = (char) (Short.parseShort(str.substring(22, 24), 16));
         
        // convert the data in the pwda array into waveform data using 
        // binary operators and casting the value to a short
        wfs[0] = (short) (((pwda[0] << 2) | ((pwda[1]>>6) & 0x03)) & 0x03ff);
        wfs[1] = (short) (((pwda[1] << 4) | ((pwda[2] >> 4) & 0x0f)) & 0x03ff);
        wfs[2] = (short) (((pwda[2] << 6) | ((pwda[3] >> 2) & 0x3f)) & 0x03ff);
        wfs[3] = (short) (((pwda[3] << 8) | ((pwda[4] >> 0) & 0xff)) & 0x03ff);

        // return the array with usable data
        return wfs;
    }  
}
