/**
 * 
 */
package iSirona;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.Timer;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

/**
 * @authors Laura Jackson
 * 			Bryan Tanner
 * 
 * DynamicChart creates a dynamically viewed chart that scrolls across the
 * screen represented the values of an array.  The class imports libraries from
 * JFreeChart which can be downloaded online at www.jfree.org/jfreechart/ *
 */
public class DynamicChart extends ApplicationFrame implements ActionListener 
{
	/**********************************\
	 * Attributes					  *
	\**********************************/
	
	// default serialVersionUID identifier
	private static final long serialVersionUID = 1L;

	// counter is used to keep track of the number of items in an array
	// so the display knows when all the values have been added
	int counter = 0;
	// repetitions keeps track of the number of times the chart is updated
	static int repetitions = 0;
	// a double array large enough to hold all the values in our textfile
	double[] data = new double[5000];

	// declare a time series instance to control dataflow
    private TimeSeries series;
    
    // the timer refreshes the display every 5ms
    private Timer timer = new Timer(5, this);
    
    // start and stop values, and speed values
    private static final String START = "Start"; 
    private static final String STOP = "Stop"; 
    private static final int FAST = 1; 
    private static final int SLOW = FAST * 500; 
    
    /**********************************\
	 * Methods						  *
	\**********************************/
    
    /**
     * constructor creates an instance and assigns a 
     * title to the display window
     *
     * @param title The frame title.
     */
    public DynamicChart(final String title) {

    	// set the title with the title passed in
        super(title);
        this.series = new TimeSeries("iSirona Data");
        
        // assign a series of data to the TimeSeriesCollection, we will add data
        // to this series
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        // create the chart to display
        final JFreeChart chart = createChart(dataset);
        
        // create buttons to control output, this one to stop and start
        final JButton run = new JButton(STOP); 
        run.addActionListener(new ActionListener() 
        { 
 
        	@Override 
        	public void actionPerformed(ActionEvent e) { 
        		String cmd = e.getActionCommand(); 
        		if (STOP.equals(cmd)) { 
        			timer.stop(); 
        			run.setText(START); 
        		} else { 
        			timer.start(); 
        			run.setText(STOP); 
        		} 
        	} 
        }); 
        
        // combobox to select speed of the graph
        final JComboBox combo = new JComboBox(); 
        combo.addItem("Fast"); 
        combo.addItem("Slow"); 
        combo.addActionListener(new ActionListener() { 
 
            @Override 
            public void actionPerformed(ActionEvent e) { 
                if ("Fast".equals(combo.getSelectedItem())) { 
                    timer.setDelay(FAST); 
                } else { 
                    timer.setDelay(SLOW); 
                } 
            } 
        }); 
        
        // create the display and add all the panels and buttons
        ChartPanel chartpanel = new ChartPanel(chart);
        chartpanel.setPreferredSize(new java.awt.Dimension(1200, 800));
        this.add(chartpanel, BorderLayout.CENTER); 
        JPanel btnPanel = new JPanel(new FlowLayout()); 
        btnPanel.add(run); 
        btnPanel.add(combo); 
        this.add(btnPanel, BorderLayout.SOUTH);
        // initial delay before scrolling data begins
        timer.setInitialDelay(100);
        
        // sets background color of chart
        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        
        // start the timekeeping
        timer.start();
    }
    
    /**
     * setValues reads from the text file to put the values in an array
     */
    public void setValues(double[] values)
    {
    	data = values;
    }


    /**
     * Creates a chart
     * 
     * @param dataset the dataset that's charted
     * @return a chart with values in it
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Dynamic Waveform Chart", 
            "Time", 
            "mV",
            dataset, 
            true, 
            true, 
            false
        );
        
        final XYPlot plot = result.getXYPlot();
        
        // set the appearance of the chart
        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);
        
        // set ranges of the chart
        ValueAxis xaxis = plot.getDomainAxis();
        xaxis.setAutoRange(true);
        
        // domain axis would show data of 90 seconds for a time
        xaxis.setFixedAutoRange(90000.0);  // 90 seconds
        xaxis.setVerticalTickLabels(true);
        
        // range axis is set manually between 0 and 1
        ValueAxis yaxis = plot.getRangeAxis();
        yaxis.setRange(0.0, 1.0);
        
        return result;
    }
    /**
     * Generates an random entry for a particular call made by time for every 1/4th of a second 
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e) {
        
    	// reset the counter each time this method is called
    	counter = 0;
    	double val;
    	
    	// if all the data has been traversed then data
    	// is set to 0, (no more input)
    	if (repetitions > data.length - 1)
     		this.series.addOrUpdate(new Millisecond(), 0);
    	else
    	{
    		val = data[repetitions];
    		this.series.add(new Millisecond(), val);   
    	}
    	// if repetitions grossly exceed dataset then terminate window
    	if(repetitions > 6000)
    	{
    		this.dispose();
    	}
    	repetitions++;   	
    }
}   