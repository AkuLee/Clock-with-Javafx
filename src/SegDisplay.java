/**
 * 7 segment display for each digit is based on the Wikipedia reference.
 */
public class SegDisplay {

    public static final boolean[] zero = new boolean[] {true, true, true, true, true, true, false};
    public static final boolean[] one = new boolean[] {false, true, true, false, false, false, false};
    public static final boolean[] two = new boolean[] {true, true, false, true, true, false, true};
    public static final boolean[] three = new boolean[] {true, true, true, true, false, false, true};
    public static final boolean[] four = new boolean[] {false, true, true, false, false, true, true};
    public static final boolean[] five = new boolean[] {true, false, true, true, false, true, true};
    public static final boolean[] six = new boolean[] {true, false, true, true, true, true, true};
    public static final boolean[] seven = new boolean[] {true, true, true, false, false, false, false};
    public static final boolean[] eight = new boolean[] {true, true, true, true, true, true, true};
    public static final boolean[] nine = new boolean[] {true, true, true, false, false, true, true};

    // top, top-right, bot-right, bot, bot-left, top-left, middle
    // a,   b,         c,         d,   e,        f,        g
    private boolean[] segments;

    /**
     * Creates a display with no sections turned on.
     */
    public SegDisplay() {
        segments = new boolean[] {false, false, false, false, false, false, false};
    }

    /**
     * Creates a display that shows a number from the start.
     * @param num a number int
     */
    public SegDisplay(int num) {
        switch(num) {
            case 0: segments = zero; break;
            case 1: segments = one; break;
            case 2: segments = two; break;
            case 3: segments = three; break;
            case 4: segments = four; break;
            case 5: segments = five; break;
            case 6: segments = six; break;
            case 7: segments = seven; break;
            case 8: segments = eight; break;
            case 9: segments = nine; break;
        }
    }

    /**
     * Returns the number that would be displayed on 7-segment
     * @return
     */
    public int getNumber() {
        if (segments == zero) {
            return 0;
        } else if (segments == one) {
            return 1;
        }else if (segments == two) {
            return 2;
        }else if (segments == three) {
            return 3;
        }else if (segments == four) {
            return 4;
        }else if (segments == five) {
            return 5;
        }else if (segments == six) {
            return 6;
        }else if (segments == seven) {
            return 7;
        }else if (segments == eight) {
            return 8;
        }else if (segments == nine) {
            return 9;
        }

        return -1;
    }

    /**
     * Change the number that would be displayed.
     * @param n
     */
    public void setSegments(int n) {
        switch(n) {
            case 0: segments = zero; break;
            case 1: segments = one; break;
            case 2: segments = two; break;
            case 3: segments = three; break;
            case 4: segments = four; break;
            case 5: segments = five; break;
            case 6: segments = six; break;
            case 7: segments = seven; break;
            case 8: segments = eight; break;
            case 9: segments = nine; break;
        }
    }

    /**
     * Get the 7 segments that would display a number
     * @return
     */
    public boolean[] getSegments() {
        return segments;
    }
}
