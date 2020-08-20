package rsa_chat;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class bigLogTest {

	@Test
	void test() {
		BigInteger test = new BigInteger("100");
		System.out.println(RSA.log2BigInt(test));
		assertEquals(RSA.log2BigInt(test), 2);
	}

}
