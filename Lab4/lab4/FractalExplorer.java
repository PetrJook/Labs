package lab4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;


public class FractalExplorer {
	 
	// размеры окна, только один параметр, так как окно квадратное
    private int size;
    
    // ссылка для обновления отображения в разных методах в процессе вычисления фрактала
    private JImageDisplay img;
    
    // ссылка для отображения других видов фракталов в будущем
    private FractalGenerator gen;

    // объект, указывающий диапазон плоскости, которая выводится на экран
    private Rectangle2D.Double range;
    
    // конструктор, инициализация параметров
    public FractalExplorer(int displaySize)
    {
        size = displaySize;
        gen = new Mandelbrot();
        range = new Rectangle2D.Double();
        gen.getInitialRange(range);
    }
    // создание интерфейса, окно и кнопка
    public void createAndShowGUI()
    {
        JFrame frame = new JFrame("Fractals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container layout = frame.getContentPane();

        layout.setLayout(new BorderLayout());

        img = new JImageDisplay(size, size);
        layout.add(img, BorderLayout.CENTER);
        
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ResetHandler());
        frame.add(resetButton, BorderLayout.SOUTH);
        
        frame.addMouseListener(new MouseHandler());
        
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    // раскрашивание и отображение фракталов
    private void drawFractal()
    {
        double xCoord = 0;
        double yCoord = 0;
        
        float iterations = 0;
        float hue = 0;
        
        int rgbColor = 0;
        
        for(int x = 0 ; x < size ; ++x)
        {
            // x - координата пикселя, xCoord - координата в плоскости фракталов
            xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
            
            for(int y = 0 ; y < size ; ++y)
            {
                // y - координата пикселя, yCoord - координата в плоскости фракталов
                yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
                
                iterations = gen.numIterations(xCoord, yCoord);
                if(iterations < 0)
                {
                    rgbColor = 0;
                }
                else
                {
                    hue = 0.7f + iterations / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                
                img.drawPixel(x, y, rgbColor);
            }
        }
        
        img.repaint();
    }
    
    // реализуем интерфейс ActionListener, обработка кнопки сброса
    private class ResetHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        { 
            range = new Rectangle2D.Double();
            gen.getInitialRange(range);
            
            drawFractal();
        }
    }
    // обработка щелчков мышью
    private class MouseHandler extends MouseAdapter 
    { 
        public void mouseClicked(MouseEvent e)
        { 
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, e.getY());
            
            gen.recenterAndZoomRange(range,xCoord, yCoord, 0.5);
            
            drawFractal();
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
