package rsa_chat;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class getBinaryRepTest {

	@Test
	void test() {
		BigInteger test = new BigInteger("343");
		
		char[] list = RSA.getBinaryRep(test);
		
		System.out.print(test.toString() + " (dec) = ");
		for(int i = list.length - 1; i >= 0; i--) {
			System.out.print(list[i]);
		}
		
		System.out.println(" (bin)");
	}

}
