import java.io.*;
import java.util.*;

public class TextSwap {
    // "I pledge my honor that I have abided by the Stevens Honor System." - Daniel Shapiro

    private static String readFile(String filename) throws Exception {
        String line;
        StringBuilder buffer = new StringBuilder();
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            buffer.append(line);
        }
        br.close();
        return buffer.toString();
    }

    private static Interval[] getIntervals(int numChunks, int chunkSize) {
        // TODO: Implement me!
        Interval[] intervals = new Interval[numChunks];
        int i = 0;
        int j = 0;
        while(i<(numChunks*chunkSize)){
            int first = i;
            int second = first + (chunkSize -1);
            Interval interval = new Interval(first, second);
            intervals[j] = interval;
            j++;
            i = second;
            i++;
        }
        return intervals;
    }

    private static List<Character> getLabels(int numChunks) {
        Scanner scanner = new Scanner(System.in);
        List<Character> labels = new ArrayList<Character>();
        int endChar = numChunks == 0 ? 'a' : 'a' + numChunks - 1;
        System.out.printf("Input %d character(s) (\'%c\' - \'%c\') for the pattern.\n", numChunks, 'a', endChar);
        for (int i = 0; i < numChunks; i++) {
            labels.add(scanner.next().charAt(0));
        }
        scanner.close();
        // System.out.println(labels);
        return labels;
    }

    private static char[] runSwapper(String content, int chunkSize, int numChunks) {
        List<Character> labels = getLabels(numChunks);
        Interval[] intervals = getIntervals(numChunks, chunkSize);
        // TODO: Order the intervals properly, then run the Swapper instances.
        Interval[] ordered = new Interval[numChunks];
        for(int i = 0; i<numChunks; i++){
            int index = (labels.get(i) - 'a');
            ordered[i] = intervals[index];
        }
        char[] buffer = new char[numChunks*chunkSize];
        Thread[] threads = new Thread[numChunks];
        for(int j = 0; j<numChunks; j++){
            threads[j] = new Thread(new Swapper(ordered[j], content, buffer, j*chunkSize));
            threads[j].start();
        }
        try{
            for(int k = 0; k<numChunks; k++){
                threads[k].join();
            }
        }catch(InterruptedException e){
            System.out.println("Error joining threads.");
        }
        return buffer;
    }

    private static void writeToFile(String contents, int chunkSize, int numChunks) throws Exception {
        char[] buff = runSwapper(contents, chunkSize, contents.length() / chunkSize);
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.print(buff);
        writer.close();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TextSwap <chunk size> <filename>");
            return;
        }
        String contents = "";
        int chunkSize = Integer.parseInt(args[0]);
        try {
            contents = readFile(args[1]);
            writeToFile(contents, chunkSize, contents.length() / chunkSize);
        } catch (Exception e) {
            System.out.println("Error with IO.");
            return;
        }
    }
}