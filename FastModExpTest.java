package rsa_chat;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class FastModExpTest {

	@Test
	void test() {
		
		BigInteger base = new BigInteger("53");
		BigInteger exp = new BigInteger("64");
		BigInteger modBase = new BigInteger("49");
		
		BigInteger res = RSA.fastModularExponentiation(base, exp, modBase);
		
		System.out.println(base.toString() + "^" + exp.toString() + " (mod " + modBase.toString() + ") = " + res.toString());
	}

}
