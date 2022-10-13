package lab;

public class Palindrome {

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			 String s = args[i];
			 if (isPalindrome(s)) System.out.println(s);
			}
	}
	public static String reverseString(String s) {
	String d = "";
	// Цикл от i до 0, если в слове 4 буквы то i будет 3 -> 2 -> 1 -> 0
	for (int i = s.length() - 1; i >= 0; i--) {
		d += s.charAt(i);
	}
	return d;
	}
	public static boolean isPalindrome(String s) {
		// Используем функцию для переворачивания строчки и проверяем строки на идентичность
		String rs = reverseString(s);
		if (rs.equals(s)) return true;
		else return false;
	}
}
