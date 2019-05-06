package chord;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1{

    //given String to convert and module it returns the id
    public static int getSha1(String toConvert, int numBitsIdentifier){
        try {
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(toConvert.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            String module = String.valueOf((int) Math.pow(2, numBitsIdentifier));

            BigInteger ht = new BigInteger(hashtext,16);
            BigInteger m = new BigInteger(module ,10);
            BigInteger result = ht.mod(m) ;
            return result.intValue();
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}