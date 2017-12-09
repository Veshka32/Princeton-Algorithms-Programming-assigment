import edu.princeton.cs.algs4.*;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] alfabeth = new char[256];
        for (char c = 0; c < 256; c++) {
            alfabeth[c] = c;
        }
        while (!BinaryStdIn.isEmpty()) {
            char current = BinaryStdIn.readChar();
            int index = -1;
            for (int i = 0; i < 256; i++) {
                if (alfabeth[i] == current) {
                    index = i;
                    break;
                }
            }
            for (int i = index; i > 0; i--) {
                alfabeth[i] = alfabeth[i - 1];
            }
            alfabeth[0] = current;
            BinaryStdOut.write(index, 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] alfabeth = new char[256];
        for (char c = 0; c < 256; c++) {
            alfabeth[c] = c;
        }

        while (!BinaryStdIn.isEmpty()) {
            char index = BinaryStdIn.readChar();
            char first = alfabeth[index];
            BinaryStdOut.write(first);
            for (char i = index; i > 0; i--) {
                alfabeth[i] = alfabeth[i - 1];
            }
            alfabeth[0] = first;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
