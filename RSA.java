package rsa_chat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSA {

	private static final double LOG_2 = Math.log(2.0);
	private static final int MAX_DIGITS_2 = 977;
	
	public static void main(String [] args) {
		BigInteger primeP = new BigInteger("9091213529597818878440658302600437485892608310328358720428512168960411528640933367824950788367956756806141");
		BigInteger primeQ = new BigInteger("8143859259110045265727809126284429335877899002167627883200914172429324360133004116702003240828777970252499");
		BigInteger n = primeP.multiply(primeQ);
		BigInteger eulerTotient = eulersTotient(primeP, primeQ);
		
		System.out.println("RSA parameters:\nGiven the following primes...");
		System.out.println("p:            " + primeP.toString());
		System.out.println("q:            " + primeQ.toString());
		System.out.println("\n...the following was calculated:");
		System.out.println("n:            " + n.toString());
		System.out.println("eulerTotient: " + eulerTotient.toString());
		
		BigInteger e;
		BigInteger d;
		while(true) {
			e = getRandomBigInt(eulerTotient);
			List<BigInteger> gcdAndInverse = extendedEuclieanAlgo(e, eulerTotient);
			BigInteger gcd = gcdAndInverse.get(0);
			if(gcd.intValue() == 1 && gcd.bitLength() == 1){
				d = gcdAndInverse.get(1);
				System.out.println("e:            " + e.toString());
				System.out.println("d:            " + d.toString());
				break;
			}
		}
		
		System.out.println("\nLet's say we have a friend that wants to send us private information"
				+ ". To allow them to, we release some of our above\ngenerated RSA information publicly for them to use."
				+ " The information that we send them is (n, e). Everything else is kept secret,\neven from our friend because" 
				+ " that would reveal the information as it would be sent over an unsafe network.");
		
		// TODO: allow custom messages, and limit the message to 1 to n-1 in size
		String plainText = "Hello this is my top secret message!";
		System.out.println("\nThe plaintext they wish to send is: \"" + plainText + "\"");
		System.out.println("Our friend does the following to safely send us the message:");
		
		String plainTextACSIIHex = convertTextToHex(plainText);
		System.out.println("\nThe plaintext, converted to ASCII values gives:\nm (hex) = " + plainTextACSIIHex);
		
		BigInteger m = new BigInteger(plainTextACSIIHex, 16);
		System.out.println("m (dec) = " + m.toString());
		System.out.println("\nNow to encrypt the message m to get the ciphertext. This is calculated as c = m^e (mod n).");
		
		BigInteger c = fastModularExponentiation(m, e, n);
		System.out.println("c = " + c);
		
		System.out.println("\nThis ciphertext is sent by your friend to you. You want to see what it says,\n"
				+ "and in order to do that we calculate: m = c^d (mod n).");
		BigInteger unencryptedText = fastModularExponentiation(c, d, n);
		System.out.println("m (hex) = " + unencryptedText.toString(16));
		System.out.println("m (dec) = " + unencryptedText.toString());
		
		System.out.println("\nNotice, the values are the same as before encryption!");
		String orginalMessage = convertHexToText(unencryptedText.toString(16));
		System.out.println("After converting back to ASCII we have:\n"
				+ "\"" + orginalMessage + "\"");
	}
	
	
	// Credit: https://stackoverflow.com/questions/923863/converting-a-string-to-hexadecimal-in-java
	private static String convertTextToHex(String text) {
		return String.format("%040x", new BigInteger(1, text.getBytes()));
	}
	
	
	private static String convertHexToText(String hex) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < hex.length(); i += 2) {
		    String str = hex.substring(i, i+2);
		    builder.append((char)Integer.parseInt(str, 16));
		}
		
		return builder.toString();
	}

	
	// Returns a list with the GCD(a,b) and the inverse of a (mod b).
	public static List<BigInteger> extendedEuclieanAlgo(BigInteger a, BigInteger b) {
		BigInteger oldr, r, olds, s, oldt, t;
		
		oldr = a;
		r = b;
		olds = new BigInteger("1");
		s = new BigInteger("0");
		oldt = new BigInteger("0");
		t = new BigInteger("1");
		
		BigInteger quotient = new BigInteger("0");
		while(!r.toString().equals("0")){
			quotient = quotient.subtract(quotient);
			BigInteger temp = oldr.divide(r);
			quotient = quotient.add(temp);
			
			List<BigInteger> list;
			list = parallelAssignment(r, oldr, quotient);
			r = list.get(0); oldr = list.get(1);
			list = parallelAssignment(s, olds, quotient);
			s = list.get(0); olds = list.get(1);
			list = parallelAssignment(t, oldt, quotient);
			t = list.get(0); oldt = list.get(1);
		}

		// If the inverse is negative, make it positive
		if(olds.compareTo(BigInteger.ZERO) < 0){
			olds = olds.mod(b);
		}
	    
	    List<BigInteger> list = new ArrayList<BigInteger>();
	    list.add(oldr); list.add(olds);
		return list;
	}

	
	// Uses temp variables to assign multiple things at once, used by the extendedEuclieanAlgo method
	private static List<BigInteger> parallelAssignment(BigInteger notOld, BigInteger old, BigInteger quotient) {
		BigInteger temp = new BigInteger("0");
		temp = temp.add(notOld);
		
		notOld = notOld.subtract(notOld); //notOld -> 0
		notOld = notOld.add(old); //notOld -> old
		
		BigInteger product = new BigInteger("0");
		product = product.add(quotient);
		product = product.multiply(temp);
		
		notOld = notOld.subtract(product); //notOld -> old - quotient*temp
		
		old = old.subtract(old);
		old = old.add(temp);
		
		List<BigInteger> returns = new ArrayList<BigInteger>();
		returns.add(notOld);
		returns.add(old);
		return returns;
	}

	
	// Returns a random integer from 1 to (eulerTotient - 1).
	public static BigInteger getRandomBigInt(BigInteger eulerTotient) {
	      BigInteger max = eulerTotient.add(BigInteger.ONE);
	      BigInteger min = BigInteger.ONE;
	      BigInteger bigInteger = max.subtract(min);
	      Random rand = new Random();
	      
	      int lenth = max.bitLength();
	      BigInteger res = new BigInteger(lenth, rand);
	      if (res.compareTo(min) == -1) {
	         res = res.add(min);
	      }
	      if (res.compareTo(bigInteger) >= 0) {
	         res = res.mod(bigInteger).add(min);
	      }
	         
		return res;
	}
	
	
	private static BigInteger eulersTotient(BigInteger p, BigInteger q){
		BigInteger pMinusOne = p.subtract(BigInteger.ONE);
		BigInteger qMinusOne = q.subtract(BigInteger.ONE);
		return pMinusOne.multiply(qMinusOne);
	}
	
	
	public static BigInteger fastModularExponentiation(BigInteger base, BigInteger exp, BigInteger modBase) {
		BigInteger result = new BigInteger("-1");
		
		List<BigInteger> intermediateResults = new ArrayList<BigInteger>();
		
		BigInteger currValue = base;
		double logVal = log2BigInt(exp);
		int arrSize = (int) (Math.floor(logVal) + 1);
		
		for(int i = 0; i < arrSize; i++) {
			intermediateResults.add(currValue);
			currValue = currValue.multiply(currValue).mod(modBase);
		}
		
		char[] binaryRep = getBinaryRep(exp);
		
		BigInteger negOne = new BigInteger("-1");
		for(int i = 0; i < arrSize; i++){
			if(binaryRep[i] == '1'){
				if(result.compareTo(negOne) == 0){
					result = intermediateResults.get(i).mod(modBase);
				}else{
					result = (intermediateResults.get(i).multiply(result)).mod(modBase);
				}
			}
		}
		
		return result;
	}
	
	
	// Credit: https://stackoverflow.com/questions/6827516/logarithm-for-biginteger
    public static double log2BigInt(BigInteger val) {
        if (val.signum() < 1)
            return val.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY;
        int blex = val.bitLength() - MAX_DIGITS_2;
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? (res + blex) / Math.log(2) * LOG_2 : res / Math.log(2);
    }


	public static char[] getBinaryRep(BigInteger value) {
		String ret = value.toString(2);
		char[] charArr = ret.toCharArray();
		//reverse the array so indexes match intermediateResults array
		for(int i = 0; i < charArr.length/2; i++) {
			char temp = charArr[i];
			charArr[i] = charArr[charArr.length - i - 1];
			charArr[charArr.length - i - 1] = temp;
		}
		
		return charArr;	
	}
}
