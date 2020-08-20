package rsa_chat;

import java.math.BigInteger;
import java.util.List;

import org.junit.jupiter.api.Test;

class extendedEuclideanTest {

	@Test
	void test() {
		BigInteger a = new BigInteger("46");
		BigInteger b = new BigInteger("239");
		
		List<BigInteger> list = RSA.extendedEuclieanAlgo(a, b);
		System.out.println("GCD(" + a.toString() + "," + b.toString() + ") = " + list.get(0).toString());
		System.out.println(a.toString() + " inverse (mod " + b.toString() + ") = " + list.get(1).toString());
	}

}
