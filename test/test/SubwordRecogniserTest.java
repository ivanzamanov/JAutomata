package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.TestCase;

import org.ivo.automata.text.SubwordRecogniser;

public class SubwordRecogniserTest extends TestCase {
    
    public class Task implements Runnable {
        
        private String word;
        private SubwordRecogniser recognizer;
        private AtomicInteger failedCount;
        private AtomicInteger completedCount;
        
        public Task(final String word, final SubwordRecogniser sr, final AtomicInteger failedCount,
                final AtomicInteger completedCount) {
            this.word = word;
            this.recognizer = sr;
            this.failedCount = failedCount;
            this.completedCount = completedCount;
        }
        
        @Override
        public void run() {
            final boolean result = recognizer.traverse(new StringReader(word));
            if (!result) {
                failedCount.incrementAndGet();
                System.out.println("Failed " + failedCount.incrementAndGet());
            }
            System.out.println("Completed " + completedCount.incrementAndGet());
            word = null;
            recognizer = null;
            failedCount = null;
            completedCount = null;
        }
    }
    
    public void test() throws IOException, InterruptedException {
        final File inputFile = new File("data", "text.txt");
        assertTrue(inputFile.exists());
        
        final FileInputStream iStream = new FileInputStream(inputFile);
        
        final byte[] bytes = new byte[(int) inputFile.length()];
        iStream.read(bytes);
        iStream.close();
        final String text = new String(bytes);
        // final String text = "abc";
        final SubwordRecogniser sr = new SubwordRecogniser(new StringReader(text));
        System.out.println("Length " + text.length());
        final int MAX_THREADS = 80;
        final LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<Runnable>(MAX_THREADS) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean offer(final Runnable e) {
                try {
                    put(e);
                } catch (final InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
                return true;
            }
        };
        final ExecutorService executor = new ThreadPoolExecutor(0, MAX_THREADS, 1, TimeUnit.SECONDS, deque);
        final AtomicInteger failedCount = new AtomicInteger(0);
        final AtomicInteger completedCount = new AtomicInteger(0);
        for (int i = 0; i < text.length(); i++) {
            for (int j = 0; j < text.length() - i; j++) {
                final String word = text.substring(j, j + i + 1);
                executor.submit(new Task(word, sr, failedCount, completedCount));
            }
        }
        
        executor.shutdown();
        // executor.awaitTermination(2, TimeUnit.MINUTES);
        
        System.out.println("Failed count " + failedCount.get());
        assertEquals(0, failedCount.get());
    }
}
