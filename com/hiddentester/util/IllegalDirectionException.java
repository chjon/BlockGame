/***************************************************************************************
 * This class is the exception that is thrown for direction operations on zero vectors *
 ***************************************************************************************/

package com.hiddentester.util;

public class IllegalDirectionException extends Exception {
	private static final String DEFAULT_MESSAGE = "Vector has no direction";

	//Constructors:

	public IllegalDirectionException () {
		super(DEFAULT_MESSAGE);
	}

	public IllegalDirectionException (String message) {
		super(message);
	}
}
