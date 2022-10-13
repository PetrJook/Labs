package lab;

public class Primes {

	public static void main(String[] args) {
		int count = 0;
		for (int i = 2; i <= 100; i++) {
			if (IsPrime(i)) {
				System.out.println(i);
				count += 1;
			}
		}
		System.out.println("Количество простых чисел: " + count);
	}
	public static boolean IsPrime(int n) {
		// Цикл от 2 до корень из n
		for (int i = 2; i <= (int) Math.pow(n, 0.5); i++) { 
			// Проверяем остаток от деления, если число делится без остатка, выходим из функции,
			// возвращая false
			if (n % i == 0)
				return false;
		}
		// Если не нашлось ни одного числа на которое делится n, то возвращаем из функции true
		return true;
	}

}
