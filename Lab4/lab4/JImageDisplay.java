package lab4;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JImageDisplay extends javax.swing.JComponent {
	
	private BufferedImage img;
	
	// конструктор, инициализируем объект BufferedImage с заданной шириной и высотой
	public JImageDisplay(int width, int height) 
	{
		img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		Dimension dim = new Dimension(width, height);
		super.setPreferredSize(dim);
	}
	
	// метод отрисовки изображения
	 protected void paintComponent(Graphics g)
	    {
	        super.paintComponent(g);
	        g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
	    }
	 // метод очистки изображения, устанавливает чёрный цвет для всех пикселей
	 public void clearImage()
	 {
		 for(int i = 0; i < img.getHeight(); ++i) 
		 {
			 for(int j = 0; j < img.getWidth(); ++j)
			 {
				 img.setRGB(j, i, 0);
			 }
		 }
	 }
	 // устанавливает цвет пикселя с координатами x, y в значение rgbColor
	 public void drawPixel(int x, int y, int rgbColor)
	 {
		 img.setRGB(x, y, rgbColor);
	 }
}
