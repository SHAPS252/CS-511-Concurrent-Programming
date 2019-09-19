public class Swapper implements Runnable {
    private int offset;
    private Interval interval;
    private String content;
    private char[] buffer;

    public Swapper(Interval interval, String content, char[] buffer, int offset) {
        this.offset = offset;
        this.interval = interval;
        this.content = content;
        this.buffer = buffer;
    }
    // "I pledge my honor that I have abided by the Stevens Honor System." - Daniel Shapiro
    @Override
    public void run() {
        // TODO: Implement me!
        int x = interval.getX();
        int y = interval.getY();
        int first = x;
        for(int i = offset; i <= (offset + (y - x)); i++){
            buffer[i] = content.charAt(first);
            first++;
        }
    }
}