package lab4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;


public class FractalExplorer {
	 
	// размеры окна, только один параметр, так как окно квадратное
    private int size;
    
    // ссылка для обновления отображения в разных методах в процессе вычисления фрактала
    private JImageDisplay img;
    
    // ссылка для отображения других видов фракталов в будущем
    private FractalGenerator mandelbrot;
    private FractalGenerator burningShip;
    private FractalGenerator tricorn;
    private FractalGenerator gen;

    // объект, указывающий диапазон плоскости, которая выводится на экран
    private Rectangle2D.Double range;
    
    // компонент окна программы
    private JFrame frame;
    // компоненты окна программы
    private JComboBox fractalsBox;
    private JButton resetButton;
    private JButton saveButton;
    
    // поле, для того, чтобы узнать, когда закончится отрисовка
    private int rows_remaining;
    // конструктор, инициализация параметров
    public FractalExplorer(int displaySize)
    {
        size = displaySize;
        
        mandelbrot = new Mandelbrot();
        burningShip = new BurningShip();
        tricorn = new Tricorn();
        gen = mandelbrot;
        
        range = new Rectangle2D.Double();
        gen.getInitialRange(range);
    }
    // создание интерфейса, окно и кнопка
    public void createAndShowGUI()
    {
        frame = new JFrame("Fractals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container layout = frame.getContentPane();

        layout.setLayout(new BorderLayout());

        img = new JImageDisplay(size, size);
        layout.add(img, BorderLayout.CENTER);
        
        ResetHandler handler = new ResetHandler();
        
        JPanel topPanel = new JPanel();
        
        JLabel panelLabel = new JLabel("Choose fractal: ");
        
        topPanel.add(panelLabel);
        
        fractalsBox = new JComboBox();
        fractalsBox.addItem(mandelbrot);
        fractalsBox.addItem(burningShip);
        fractalsBox.addItem(tricorn);
        fractalsBox.addActionListener(handler);
        
        topPanel.add(fractalsBox);
        
        frame.add(topPanel, BorderLayout.NORTH);
        
        JPanel bottomPanel = new JPanel();
        
        resetButton = new JButton("Reset");
        resetButton.setActionCommand("reset");
        resetButton.addActionListener(handler);
        bottomPanel.add(resetButton);
        
        saveButton = new JButton("Save");
        saveButton.setActionCommand("save");
        saveButton.addActionListener(handler);
        bottomPanel.add(saveButton);
        
        frame.add(bottomPanel, BorderLayout.SOUTH);
        
        frame.addMouseListener(new MouseHandler());
        
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    // раскрашивание и отображение фракталов
    private void drawFractal()
    {   
    	enableUI(false);
    	rows_remaining = size;
        for(int y = 0 ; y < size ; ++y)
        {
        	FractalWorker worker = new FractalWorker(y);
        	worker.execute();
        }
    }
    
    private void enableUI(boolean val)
    {
    	fractalsBox.setEnabled(val);
    	saveButton.setEnabled(val);
    	resetButton.setEnabled(val);
    }
    
    
    // реализуем интерфейс ActionListener, обработка кнопки сброса
    private class ResetHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        { 
        	String command = e.getActionCommand();
        	
        	if (e.getSource() == fractalsBox)
        	{
        		String selectedItem = fractalsBox.getSelectedItem().toString();
        		
        		if (selectedItem.equals(mandelbrot.toString()))
        		{
        			gen = mandelbrot;
        		}
        		
        		else if (selectedItem.equals(burningShip.toString()))
        		{
        			gen = burningShip;
        		}
        		
        		else if (selectedItem.equals(tricorn.toString()))
        		{
        			gen = tricorn;
        		}
        		
                range = new Rectangle2D.Double();
                gen.getInitialRange(range);
                
                drawFractal();
        	}
        	
        	else if (command.equals("save"))
        	{
        		JFileChooser fileChooser = new JFileChooser();
    			FileFilter filter = new FileNameExtensionFilter("PNG Images", "png");
    			fileChooser.setFileFilter(filter);
    			fileChooser.setAcceptAllFileFilterUsed(false);
        		int operation = fileChooser.showSaveDialog(frame);
        		
        		if (operation == JFileChooser.APPROVE_OPTION)
        		{
        			try
        			{
        			File directory = fileChooser.getSelectedFile();
        			String path = directory.getPath();
                    if(!path.endsWith(".png"))
                    {
                        directory = new File(path + ".png");
                    }
        			
        			ImageIO.write(img.getImage(), "png", directory);
        			
        			}
        			catch (IOException exception)
        			{
        					JOptionPane.showMessageDialog(frame, "Error: " + exception.getMessage(), "Cannot Save Image", JOptionPane.ERROR_MESSAGE);
        			}
        			
        		}
        	}
        	else if (command.equals("reset"))
        	{
                range = new Rectangle2D.Double();
                gen.getInitialRange(range);
                
                drawFractal();
        	}

        }
    }
    // обработка щелчков мышью
    private class MouseHandler extends MouseAdapter 
    { 
        public void mouseClicked(MouseEvent e)
        { 
        	if (rows_remaining != 0)
        		return;
        	
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, e.getY());
            
            gen.recenterAndZoomRange(range,xCoord, yCoord, 0.5);
            
            drawFractal();
        } 
    }
    
    private class FractalWorker extends SwingWorker<Object, Object>
    {

    	int y;
    	int[] line;
    	
    	public FractalWorker(int y)
    	{
    		this.y = y;
    	}
    	
		@Override
		protected Object doInBackground() throws Exception 
		{
			line = new int[size];
			int rgbColor;
	    	int iterations;
		    float hue = 0;
			double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
			for(int x = 0 ; x < size ; ++x)
            {
                // x - координата пикселя, xCoord - координата в плоскости фракталов
                double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
                
                iterations = gen.numIterations(xCoord, yCoord);
                if(iterations < 0)
                {
                    rgbColor = 0;
                    line[x] = rgbColor;
                }
                else
                {
                    hue = 0.7f + iterations / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    line[x] = rgbColor;
                }
                
            }
			return null;
		}
		
		@Override
	       protected void done()
	       {
				for(int x = 0 ; x < size ; ++x)
				{
					img.drawPixel(x, y, line[x]);
				}
				img.repaint(0, 0, y, size, 1);
				--rows_remaining;
				if (rows_remaining == 0)
					enableUI(true);
	       }
		
    	
    }
    
    
    // точка входа в программу
    public static void main(String[] args) 
    {
        FractalExplorer explorer = new FractalExplorer (800);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    }
}
