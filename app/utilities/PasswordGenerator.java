package utilities;

import java.util.Random;

public final class PasswordGenerator {
	private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyz1234567890";

	private static int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }
	
	public static String random(int RANDOM_STRING_LENGTH) {
	  StringBuffer randStr = new StringBuffer();
	  
	  for(int i=0; i<RANDOM_STRING_LENGTH; i++){
	      int number = getRandomNumber();
	      char ch = CHAR_LIST.charAt(number);
	      randStr.append(ch);
	  }
	  
	  return randStr.toString();
	}
}