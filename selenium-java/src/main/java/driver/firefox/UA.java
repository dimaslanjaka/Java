package driver.firefox;

import java.util.Random;

public class UA {
	static String[] firefox = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:85.0) Gecko/20100101 Firefox/85.0"};
	static String[] chrome = new String[]{"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36"};
	static String[] ie = new String[]{"Mozilla/5.0 CK={} (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko"};

	public static String getRandom(String[] array) {
		int rnd = new Random().nextInt(array.length);
		return array[rnd];
	}

	public static String get() {
		return getRandomString();
	}

	public static String getRandomString() {
		Random r = new Random();

		int i = r.nextInt() % 2;

		switch (i) {
			case 0:
				return getRandom(firefox);
			case 1:
				return getRandom(chrome);
			case 2:
				return getRandom(ie);
			default:
				break;
		}
		return null;
	}
}
