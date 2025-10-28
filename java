package coa.alu;

public class ALU {

    private static final int WORD_SIZE = 32; // Simulating a 32-bit architecture

    // --- 1. Logic Gates (Private) ---
    private boolean gateAnd(boolean a, boolean b) {
        return a && b;
    }

    private boolean gateOr(boolean a, boolean b) {
        return a || b;
    }

    private boolean gateXor(boolean a, boolean b) {
        return a != b;
    }

    private boolean gateNot(boolean a) {
        return !a;
    }

    // --- 2. Combinational Logic Circuits (Adders) ---
    private boolean[] halfAdder(boolean a, boolean b) {
        boolean sum = gateXor(a, b);
        boolean carry = gateAnd(a, b);
        return new boolean[]{sum, carry};
    }

    private boolean[] fullAdder(boolean a, boolean b, boolean carryIn) {
        boolean[] halfSum1 = halfAdder(a, b);
        boolean[] halfSum2 = halfAdder(halfSum1[0], carryIn);
        boolean carryOut = gateOr(halfSum1[1], halfSum2[1]);
        return new boolean[]{halfSum2[0], carryOut};
    }

    // --- 3. Arithmetic Operations (Public) ---

    public boolean[] add(boolean[] a, boolean[] b) {
        boolean[] result = new boolean[WORD_SIZE];
        boolean carry = false;
        for (int i = WORD_SIZE - 1; i >= 0; i--) {
            boolean[] sum = fullAdder(a[i], b[i], carry);
            result[i] = sum[0];
            carry = sum[1];
        }
        return result;
    }

    private boolean[] twosComplement(boolean[] n) {
        boolean[] inverted = new boolean[WORD_SIZE];
        for (int i = 0; i < WORD_SIZE; i++) {
            inverted[i] = gateNot(n[i]);
        }
        boolean[] one = new boolean[WORD_SIZE];
        one[WORD_SIZE - 1] = true;
        return add(inverted, one);
    }

    public boolean[] subtract(boolean[] a, boolean[] b) {
        return add(a, twosComplement(b));
    }

    public boolean[] multiply(boolean[] a, boolean[] b) {
        // NOTE: This is a functional "cheat". A real ALU would use
        // shift-and-add logic. We convert to int for simplicity.
        int intA = binaryToInt(a);
        int intB = binaryToInt(b);
        return intToBinary(intA * intB);
    }

    public boolean[] divide(boolean[] a, boolean[] b) {
        // NOTE: This is a functional "cheat". A real ALU would use
        // repeated subtraction. We convert to int for simplicity
        // and to easily handle division by zero.
        int intA = binaryToInt(a);
        int intB = binaryToInt(b);
        if (intB == 0) { // This was the line with the error before
            throw new ArithmeticException("Division by zero");
        }
        return intToBinary(intA / intB);
    }

    // --- 4. Logical Operations (Public) ---

    public boolean[] and(boolean[] a, boolean[] b) {
        boolean[] result = new boolean[WORD_SIZE];
        for (int i = 0; i < WORD_SIZE; i++) {
            result[i] = gateAnd(a[i], b[i]);
        }
        return result;
    }

    public boolean[] or(boolean[] a, boolean[] b) {
        boolean[] result = new boolean[WORD_SIZE];
        for (int i = 0; i < WORD_SIZE; i++) {
            result[i] = gateOr(a[i], b[i]);
        }
        return result;
    }

    public boolean[] xor(boolean[] a, boolean[] b) {
        boolean[] result = new boolean[WORD_SIZE];
        for (int i = 0; i < WORD_SIZE; i++) {
            result[i] = gateXor(a[i], b[i]);
        }
        return result;
    }

    public boolean[] not(boolean[] a) {
        boolean[] result = new boolean[WORD_SIZE];
        for (int i = 0; i < WORD_SIZE; i++) {
            result[i] = gateNot(a[i]);
        }
        return result;
    }

    // --- 5. Utility / Conversion Methods ---

    public static boolean[] intToBinary(int n) {
        boolean[] binary = new boolean[WORD_SIZE];
        for (int i = WORD_SIZE - 1; i >= 0; i--) {
            binary[i] = (n & (1 << (WORD_SIZE - 1 - i))) != 0;
        }
        return binary;
    }

    public static int binaryToInt(boolean[] binary) {
        int n = 0;
        for (int i = 0; i < WORD_SIZE; i++) {
            if (binary[i]) {
                n |= (1 << (WORD_SIZE - 1 - i));
            }
        }
        return n;
    }

    public static String binaryToString(boolean[] binary) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < binary.length; i++) {
            sb.append(binary[i] ? '1' : '0');
            if ((i + 1) % 4 == 0 && i < binary.length - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}
