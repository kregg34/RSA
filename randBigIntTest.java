package rsa_chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class randBigIntTest {

	@Test
	void test() {
		BigInteger euler = new BigInteger("4324");
		
		boolean fail = false;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i = 0; i < 100000; i++) {
			BigInteger returned = RSA.getRandomBigInt(euler);
			if(returned.intValue() < min) {
				min = returned.intValue();
			}
			
			if(returned.intValue() > max) {
				max = returned.intValue();
			}
			
			if(returned.intValue() >= 45667458 || returned.intValue() <= 0) {
				fail = true;
				break;
			}
		}
		
		System.out.println(min + ", " + max);
		if(fail) {
			fail();
		}else {
			assertEquals(1, 1);
		}
	}

}
