/**
 * 
 */
package iSirona;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.*;
import java.awt.geom.*;
import javax.swing.*;

import org.jfree.ui.RefineryUtilities;

/**
 * @authors Laura Jackson
 * 			Bryan Tanner
 * 
 * The PlotGraph class is used to plot a static graph
 * given an array of data 
 */
public class PlotGraph 
{     

	/**********************************\
	 * Attributes					  *
	\**********************************/
	// create an instance of the plotPanel to render the display on
	PlotPanel plotPanel;
	// two double arrays to hold values for x and y coordinates
	double[] x, y;
	 

	/**********************************\
 	 * Methods						  *
	\**********************************/
	/**
	 * setData is used to pass in the arrays to use as x and y coordinates
	 * 
	 * @param x Values used to plot along the x axis
	 * @param y Values used to plot along the y axis
	 */
	public void setData(double[] x, double[] y)
	{
		plotPanel = new PlotPanel(x, y);
		// assign the passed in arrays to be used in other methods
		this.x = x;
		this.y = y;
	}  
	
	/**
	 * getButtonPanel returns a panel of two buttons to
	 * be used to select the dynamic and static graphs
	 * @return a JPanel containing the two buttons
	 */
	public JPanel getButtonPanel() 
	{        
		JButton stat = new JButton("Static");           
		stat.addActionListener(new ActionListener() 
		{          
			@Override
			public void actionPerformed(ActionEvent e) 
			{                                               
				plotPanel.setData(x, y);            
			}      
		});   
		
		JButton dyna = new JButton("Dynamic");           
		dyna.addActionListener(new ActionListener() 
		{          
			@Override
			public void actionPerformed(ActionEvent e) 
			{                                               
		        final DynamicChart demo = new DynamicChart("Dynamic Line And TimeSeries Chart");
		        demo.pack();
		        demo.setValues(y);
		        RefineryUtilities.centerFrameOnScreen(demo);
		        demo.setVisible(true);            
			}      
		});
		
		JPanel panel = new JPanel(new GridBagLayout());        
		GridBagConstraints gbc = new GridBagConstraints();       
		
		panel.setBorder(BorderFactory.createEtchedBorder());        
		gbc.weightx = 1.0;        
		panel.add(stat, gbc);
		panel.add(dyna, gbc);
		return panel;    
	}     
	
	/**
	 * getContent returns the JPanel used to draw inside of
	 * 
	 * @return JPanel that will be used to draw the graphs
	 */
	public JPanel getContent() 
	{        
		//double[] x = getData((double) -MAX, MAX);        
		//double[] y = getData((double) -MAX, MAX);        
		//plotPanel = new PlotPanel(x, y);        
		return plotPanel;    
	}  
	

}


/**
 * @authors Laura Jackson
 * 			Bryan Tanner
 *
 * PlotPanel is the class that actually applies the plots to 
 * the JPanel
 */
class PlotPanel extends JPanel 
{     
	// serialVersionUID
	private static final long serialVersionUID = 1L;
	// arrays to hold x and y values to plot
	double[] x, y;
	// minimum value of the x and y axes determined dynamically
	double xMin, xMax;
	// for the purpose of this project the yMin is set to 1 and
	// yMax is set to 0 to allow an ideal viewing window.  These
	// values could be changed to double and dynamically set like the x values
	final double yMin = 1; 
	final double yMax = 0;  
	// PAD allows the borders to be drawn
	final int PAD = 20;
	
	/**
	 * PlotPanel sets the arrays to be used to plot the graph
	 * @param x Values used to plot the x axis
	 * @param y Values used to plot the y axis
	 */
	public PlotPanel(double[] x, double[] y) 
	{        
		setData(x, y);    
	}     
	
	/**
	 * paintComponent renders the display for the user
	 * @SuppressWarnings the code to dynamically set y axis values isn't used
	 *  but is important to keep for future use when y axis may exceed the range of (0,1)
	 */
	@SuppressWarnings("unused")
	protected void paintComponent(Graphics g) 
	{        
		// call the super class' method
		super.paintComponent(g);
		// translate the Graphics to 2D graphics
		Graphics2D g2 = (Graphics2D) g;        
		// turn on Antialiasing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,                
				RenderingHints.VALUE_ANTIALIAS_ON);   
		// get width and height of panel
		int w = getWidth();        
		int h = getHeight();  
		// establish scales for x and y axis
		double xScale = (w - 2 * PAD) / (xMax - xMin);        
		double yScale = (h - 2 * PAD) / (yMax - yMin);        
		
		// set up the axes origins and offsets
		Point2D.Double origin = new Point2D.Double();      
		Point2D.Double offset = new Point2D.Double();     
		if (xMax < 0) 
		{  
			origin.x = w - PAD;   
			offset.x = origin.x - xScale * xMax;  
		} 
		else if (xMin < 0) 
		{            
			origin.x = PAD - xScale * xMin;            
			offset.x = origin.x;        
		} 
		else 
		{            
			origin.x = PAD;            
			offset.x = PAD - xScale * xMin;        
		}        
		if (yMax < 0) 
		{            
			origin.y = h - PAD;            
			offset.y = origin.y - yScale * yMax;        
		} 
		else if (yMin < 0) 
		{            
			origin.y = PAD - yScale * yMin;            
			offset.y = origin.y;        
		} 
		else 
		{            
			origin.y = PAD;            
			offset.y = PAD - yScale * yMin;        
		}        
     
		// draw abcissa        
		g2.draw(new Line2D.Double(PAD, origin.y, w - PAD, origin.y));        
		// draw ordinate        
		g2.draw(new Line2D.Double(origin.x, PAD, origin.x, h - PAD));        
		g2.setPaint(Color.red);        
		// mark origin        
		g2.fill(new Ellipse2D.Double(origin.x - 2, origin.y - 2, 4, 4));         
		// now start plotting the data on the graph        
		g2.setPaint(Color.blue); 
		// 4 doubles are used to represent 2 points to draw a line between
		double x1, y1, x2, y2;
		x2 = 0;
		y2 = 0;
		// declare a 2D line that will be drawn between each point
		Line2D line;
		// for each x coordinate, plot the point and draw a line 
		// between the current plot and the previous point plotted
		for (int i = 0; i < x.length; i++) 
		{           
			x1 = offset.x + xScale * x[i];            
			y1 = offset.y + yScale * y[i]; 
			
			// for the first and last elements in the array
			// we don't want to draw a line connecting to
			// the previous or post points
			if ((i != 0) && (i != (x.length - 1)))
			{
				// create the line and update x2 and y2 for use next iteration
				line = new Line2D.Double(x1, y1, x2, y2);
				x2 = x1;
				y2 = y1;
				
				// draw the line on the graph
				g2.draw(line);
			}
			// else draw a point for the first and lost plot points
			else
			{
				g2.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
				x2 = x1;
				y2 = y1;
			}
		}       
		// draw the min and max values in text on the panel   
		g2.setPaint(Color.black);    
		Font font = g2.getFont();    
		FontRenderContext frc = g2.getFontRenderContext();   
		LineMetrics lm = font.getLineMetrics("0", frc);  
		String s = String.format("%.1f", xMin);   
		float width = (float) font.getStringBounds(s, frc).getWidth();    
		double x = offset.x + xScale * xMin;   
		s = String.format("%.1f", xMax);    
		width = (float) font.getStringBounds(s, frc).getWidth();   
		x = offset.x + xScale * xMax;   
		g2.drawString(s, (float) x - width, (float) origin.y + lm.getAscent());   
		s = String.format("%.1f", yMin);     
		width = (float) font.getStringBounds(s, frc).getWidth();    
		double y = offset.y + yScale * yMin;    
		g2.drawString(s, (float) origin.x + 1, (float) y + lm.getAscent());   
		s = String.format("%.1f", yMax);      
		width = (float) font.getStringBounds(s, frc).getWidth();    
		y = offset.y + yScale * yMax;       
		g2.drawString(s, (float) origin.x + 1, (float) y);      
	}     
	
	/**
	 * setData assigns the double arrays to the values passed to them
	 * 
	 * @param x Values for the x axis
	 * @param y Values for the y axis
	 */
	public void setData(double[] x, double[] y) 
	{    
		// make sure the arrays are the same length
		if (x.length != y.length) 
		{            
			throw new IllegalArgumentException("x and y data arrays must be same length.");
		}        
		this.x = x;        
		this.y = y;       
		// get the max and min for x values
		double[] xVals = getExtremeValues(x);    
		xMin = xVals[0];      
		xMax = xVals[1];    
 
		// commented this out because our min and max are
		// predefined as 0 and 1 respectively
		//double[] yVals = getExtremeValues(y);   
		//yMin = yVals[1];   
		//yMax = yVals[0]; 
		
		// repaint the display
		repaint();   
	}     
	
	/**
	 * getExtremeValues finds the min and max values in an array
	 * @param d Array of doubles to get the max and min values of
	 * @return A 2 valued array with the min and max values of the parameter array
	 */
	private double[] getExtremeValues(double[] d) 
	{        
		double min = Double.MAX_VALUE;       
		double max = -min;   
		for (int i = 0; i < d.length; i++) 
		{        
			if (d[i] < min) 
			{        
				min = d[i];     
			}    
			if (d[i] > max) 
			{          
				max = d[i];            
			} 
		}        
	return new double[]{min, max};    
	}
}

