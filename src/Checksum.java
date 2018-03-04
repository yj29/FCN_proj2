
/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */
public class Checksum {
    /**
     * This mehtod computes the checksum
     *
     * @param data
     * @return
     */
    public static String checkSum(String data) {
        String charAtDataPos1 = Integer.toBinaryString(data.charAt(0));
        String charAtDataPos2 = Integer.toBinaryString(data.charAt(1));

        String binarySumOfData = sumBinary(charAtDataPos1, charAtDataPos2);
        String checkSum = onesComplement(binarySumOfData);

        return checkSum;
    }

    /**
     * Contributes to the checksum computation by doing binary addition
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String sumBinary(String s1, String s2) {
        StringBuilder sb1 = new StringBuilder(s1);
        StringBuilder sb2 = new StringBuilder(s2);
        StringBuilder binarySum = new StringBuilder();
        int carry = 0;
        for (int i = s1.length() - 1; i >= 0; i--) {
            int charVal1 = Integer.parseInt(sb1.charAt(i) + "");
            int charVal2 = Integer.parseInt(sb2.charAt(i) + "");

            int sum = charVal1 + charVal2 + carry;

            if (sum > 1) {
                binarySum.append(sum % 2);
                carry = 1;
            } else {
                binarySum.append(sum);
                carry = 0;
            }
        }
        binarySum = binarySum.reverse();

        if (carry == 1) {
            return sumBinary(binarySum.toString(), "0000001");
        }
        return binarySum.toString();
    }

    /**
     * Takes one's complement of the binary to finalize the checksum value
     *
     * @param s
     * @return
     */
    public static String onesComplement(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '0') {
                sb.setCharAt(i, '1');
            } else {
                sb.setCharAt(i, '0');
            }
        }
        return sb.toString();
    }

    /**
     * Checks if the message has correct checksum
     *
     * @param data
     * @param checksum
     * @return
     */
    public static boolean isChecksumValid(String data, String checksum) {
        String charAtDataPos1 = Integer.toBinaryString(data.charAt(0));
        String charAtDataPos2 = Integer.toBinaryString(data.charAt(1));

        String sumOfDataAndChecksum = sumBinary(charAtDataPos1, charAtDataPos2);
        sumOfDataAndChecksum = sumBinary(sumOfDataAndChecksum, checksum);

        return isAllBitsSet(sumOfDataAndChecksum);
    }

    public static boolean isAllBitsSet(String data) {
        for (char ch : data.toCharArray()) {
            if (ch != '1') return false;
        }
        return true;
    }

}