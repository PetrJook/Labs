package lab4;

import java.awt.geom.Rectangle2D.Double;

// Класс, реализующий создание фрактала Мандельброта

public class Mandelbrot extends FractalGenerator {
	
	public static final int MAX_ITERATIONS = 2000;
	
	// установка изначального диапазона фрактала
	@Override
	public void getInitialRange(Double range) {
		range.x = -2;
        range.y = -1.5;
        range.width = 3;
        range.height = 3;

	}

	@Override
	public int numIterations(double x, double y) {
		// текущее количество итераций
		int iterations = 0;
		// zr - действительная часть числа, zi - мнимая
		double zr = 0;
		double zi = 0;
		
		// значения на следующей итерации
		double zr2;
		double zi2;
		/** алгоритм вычисления фрактала Мандельброта
		 * c = x + i * y
		 * x_n+1 = x_n * x_n - y_n * y_n + x0
		 * y_n+1 = 2 * x_n * y_n + y0
		**/
		while(iterations < MAX_ITERATIONS && zr * zr + zi * zi < 4)
		{
			zr2 = zr * zr - zi * zi + x;
			zi2 = 2 * zr * zi + y;
			zr = zr2;
			zi = zi2;
			iterations += 1;
		}
		
		if (iterations == MAX_ITERATIONS)
			return -1;
		return iterations;
	}
	
	@Override
	 public String toString()
	    {
	        return "Mandelbrot";
	    }
}
