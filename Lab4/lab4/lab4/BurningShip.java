package lab4;

import java.awt.geom.Rectangle2D.Double;

public class BurningShip extends FractalGenerator {
	
	public static final int MAX_ITERATIONS = 2000;
	
	// установка изначального диапазона фрактала
	@Override
	public void getInitialRange(Double range) {
		range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;

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
		
		while(iterations < MAX_ITERATIONS && zr * zr + zi * zi < 4)
		{
			zr2 = zr * zr - zi * zi + x; // возведение в квадрат настоящей части, модуль не требуется
			zi2 = Math.abs(2* zr * zi) + y;
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
	        return "Burning Ship";
	    }

}
