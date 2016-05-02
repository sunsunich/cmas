package org.cmas.presentation.service.user;

import java.security.SecureRandom;

public class PasswordServiceImpl implements PasswordService {

    /**
     * Password Length:
     5 Points: Less or eq than 4 characters
     10 Points: 5 to 7 characters
     25 Points: 8 or more
     Letters:
     0 Points: No letters
     10 Points: Letters are all lower case
     20 Points: Letters are upper case and lower case
     Numbers:
     0 Points: No numbers
     10 Points: 1 number
     20 Points: 3 or more numbers
     Characters:
     0 Points: No characters
     10 Points: 1 character
     25 Points: More than 1 character
     Bonus:
     2 Points: Letters and numbers
     3 Points: Letters, numbers, and characters
     5 Points: Mixed case letters, numbers, and characters
     Password strength is measure by the percent of the above:
     >= 90: Very Secure
     >= 80: Secure
     >= 70: Very Strong
     >= 60: Strong
     >= 50: Average
     >= 25: Weak
     >= 0: Very Weak
     * @param password - password which strenth is measured
     * @return PasswordStrength
     */
	@Override
	@SuppressWarnings({"MagicNumber"})
	public PasswordStrength measurePasswordStrength(String password) {

        if(password.isEmpty()){
            return PasswordStrength.NONE;
        }

		int points = calcPoints(password);

		if (points < 25) {
			return PasswordStrength.VERY_WEAK;
		} else if (points < 50) {
			return PasswordStrength.WEAK;
		} else if (points < 60) {
			return PasswordStrength.AVERAGE;
		} else if (points < 70) {
			return PasswordStrength.STRONG;
		}else if (points < 80) {
			return PasswordStrength.VERY_STRONG;
		} else if (points < 90) {
			return PasswordStrength.SECURE;
		}else {
			return PasswordStrength.VERY_SECURE;
		}
	}

    @SuppressWarnings({"MagicNumber", "OverlyComplexMethod", "OverlyLongMethod", "ConstantConditions"})
	private int calcPoints(String password){
        int points = 0;
        int length = password.length();
        if(length <= 4){
            points += 5;
        } else {//noinspection ConstantOnLeftSideOfComparison
			if (5 <= length && length <= 7) {
				points += 10;
			} else {
				points += 25;
			}
		}

		int uppercaseLetterCount = 0;
		int lowercaseLetterCount = 0;
		int numberCount = 0;
		int otherCharCount = 0;
		for (int i = 0; i < length; i++) {
			char ch = password.charAt(i);
			if (Character.isDigit(ch)) {
				numberCount++;
			} else if (Character.isUpperCase(ch)) {
				uppercaseLetterCount++;
			} else if (Character.isLowerCase(ch)) {
				lowercaseLetterCount++;
			} else{
				otherCharCount++;
			}
		}

		boolean hasMixedLetters = uppercaseLetterCount > 0 && lowercaseLetterCount > 0;
		boolean hasLetters = uppercaseLetterCount > 0 || lowercaseLetterCount > 0;
		if (hasMixedLetters) {
			points += 20;
		} else if (hasLetters) {
			points += 10;
		}

		boolean hasOtherChars = otherCharCount > 0;
		if (numberCount >= 1 && numberCount <= 2) {
			points += 10;
		} else if (numberCount >= 3) {
			if (hasLetters || hasOtherChars) {
				points += 20;
			}
			else{
				points += 10;
			}
		}

		if (otherCharCount == 1) {
			points += 10;
		} else if (otherCharCount > 1) {
			points += 25;
		}

		boolean hasNumbers = numberCount > 0;
		if (hasMixedLetters && hasNumbers && hasOtherChars) {
			points += 5;
		} else {
			if (hasLetters && hasNumbers && hasOtherChars) {
				points += 3;
			} else {
				if (hasLetters && hasNumbers) {
					points += 2;
				}
			}
		}
        return points;
    }
   
	private static final int BEG_ASCII_CODE = 33;
	private static final int ZERO_CHAR_CODE = 48;
	private static final int NINE_CHAR_CODE = 57;
	private static final int UPPER_A_CHAR_CODE = 65;
	private static final int UPPER_Z_CHAR_CODE = 90;
	private static final int LOWER_A_CHAR_CODE = 97;
	private static final int LOWER_Z_CHAR_CODE = 123;
	private static final int END_ASCII_CODE = 126;

    private final SecureRandom rnd = new SecureRandom();

	private char genRandomChar(int lowerCode, int upperCode) {
		int rndInt = rnd.nextInt(upperCode - lowerCode + 1) + lowerCode;
		return (char)rndInt;
	}

	private char genRandomUpLetter() {
		return genRandomChar(UPPER_A_CHAR_CODE, UPPER_Z_CHAR_CODE);
	}

	private char genRandomLowLetter() {
		return genRandomChar(LOWER_A_CHAR_CODE, LOWER_Z_CHAR_CODE);
	}

	private char genRandomNumber() {
		return genRandomChar(ZERO_CHAR_CODE, NINE_CHAR_CODE);
	}

	private char genRandomOtherChar() {
		int otherCharGroup = rnd.nextInt(4);
		switch (otherCharGroup) {
			case 0:
				return genRandomChar(BEG_ASCII_CODE, ZERO_CHAR_CODE - 1);
			case 1:
				return genRandomChar(NINE_CHAR_CODE + 1, UPPER_A_CHAR_CODE - 1);
			case 2:
				return genRandomChar(UPPER_Z_CHAR_CODE + 1, LOWER_A_CHAR_CODE - 1);
			case 3:
				return genRandomChar(LOWER_Z_CHAR_CODE + 1, END_ASCII_CODE);
			default:
				throw new IllegalStateException("random generation error");
		}
	}

    @SuppressWarnings({"MagicNumber"})
	@Override
    public String generatePassword() {
		StringBuilder sb = new StringBuilder();
		int passwdPattern = rnd.nextInt(8);
		switch (passwdPattern) {
			case 0: sb.append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomNumber())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					;
				break;
			case 1: sb.append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomNumber())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					;
				break;
			case 2: sb.append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomNumber())
					  .append(genRandomLowLetter())
					;
				break;
			case 3: sb.append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomNumber())
					  .append(genRandomUpLetter())
					;
				break;
			case 4: sb.append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomNumber())
					  .append(genRandomLowLetter())
					  .append(genRandomNumber())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					;
				break;
			case 5: sb.append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomNumber())
					  .append(genRandomUpLetter())
					  .append(genRandomNumber())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					;
				break;
			case 6: sb.append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomNumber())
					  .append(genRandomLowLetter())
					  .append(genRandomNumber())
					  .append(genRandomLowLetter())
					;
				break;
			case 7: sb.append(genRandomUpLetter())
					  .append(genRandomLowLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomUpLetter())
					  .append(genRandomNumber())
					  .append(genRandomUpLetter())
					  .append(genRandomNumber())
					  .append(genRandomUpLetter())
					;
				break;
			default: throw new IllegalStateException("random generation error");
		}
        return sb.toString();
    }
}
