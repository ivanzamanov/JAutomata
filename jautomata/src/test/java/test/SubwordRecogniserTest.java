package test;

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

		public Task(final String word, final SubwordRecogniser sr,
				final AtomicInteger failedCount,
				final AtomicInteger completedCount) {
			this.word = word;
			this.recognizer = sr;
			this.failedCount = failedCount;
		}

		@Override
		public void run() {
			final boolean result = recognizer.traverse(new StringReader(word));
			if (!result) {
				failedCount.incrementAndGet();
				System.out.println("Failed " + failedCount.incrementAndGet());
			}
			word = null;
			recognizer = null;
			failedCount = null;
		}
	}

	private static final String TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
			+ "Nunc dignissim orci sem, et convallis nisi hendrerit vestibulum. "
			+ "Morbi suscipit purus diam, et imperdiet felis ultricies vitae. "
			+ "Sed viverra augue non elit pellentesque, eu tempor dui lacinia. "
			+ "Nam pellentesque urna eget magna interdum ornare. "
			+ "Sed varius leo vitae tempus fermentum. "
			+ "Aenean enim nisi, sollicitudin hendrerit felis ut, bibendum mattis massa. Duis interdum maximus mauris, nec luctus massa eleifend sed. "
			+ "Vivamus sit amet nibh non augue venenatis vulputate vitae non nibh. "
			+ "Fusce id ultricies dolor. "
			+ "Suspendisse potenti. Donec ut porta ipsum, sit amet euismod arcu. In semper ipsum vel gravida fermentum.";

	public void test() throws IOException, InterruptedException {
		final SubwordRecogniser sr = new SubwordRecogniser(new StringReader(
				TEXT));
		System.out.println("Length " + TEXT.length());
		final int MAX_THREADS = 80;
		final LinkedBlockingDeque<Runnable> deque = new LinkedBlockingDeque<Runnable>(
				MAX_THREADS) {
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
		final ExecutorService executor = new ThreadPoolExecutor(0, MAX_THREADS,
				1, TimeUnit.SECONDS, deque);
		final AtomicInteger failedCount = new AtomicInteger(0);
		final AtomicInteger completedCount = new AtomicInteger(0);
		for (int i = 0; i < TEXT.length(); i++) {
			for (int j = 0; j < TEXT.length() - i; j++) {
				final String word = TEXT.substring(j, j + i + 1);
				executor.submit(new Task(word, sr, failedCount, completedCount));
			}
		}

		executor.shutdown();
		// executor.awaitTermination(2, TimeUnit.MINUTES);

		System.out.println("Failed count " + failedCount.get());
		assertEquals(0, failedCount.get());
	}
}
