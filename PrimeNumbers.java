import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class PrimeNumbers {
  private static final int LIMIT = 100000000;
  public static final int NUM_THREADS = 8;
  public static final String FILENAME = "primes.txt";

  public static void main(String[] args) throws InterruptedException, IOException {
    // Create shared counter/sieve among threads
    Sieve sieve = new Sieve(1, LIMIT);

    // Create and start threads
    Thread[] threads = new PrimeThread[NUM_THREADS];
    final long executionStartTime = System.currentTimeMillis();
    for (int i = 0; i < NUM_THREADS; i++) {
      threads[i] = new PrimeThread(sieve, LIMIT);
      threads[i].start();
    }
    // Wait for threads to die
    for (int i = 0; i < NUM_THREADS; i++) {
      threads[i].join();
    }

    // Find sum of primes and number of primes
    boolean[] sieveArray = sieve.getSieveArray();
    long sumOfPrimes = 0;
    long numPrimesFound = 0;
    for (int i = 0; i < sieveArray.length; i++) {
      if (sieveArray[i]) {
        sumOfPrimes += i;
        numPrimesFound++;
      }
    }
    // Find 10 biggest primes
    long[] maxPrimes = new long[10];
    int index = maxPrimes.length - 1;
    for (int i = sieveArray.length - 1; i > 0; i--) {
      if (sieveArray[i]) {
        maxPrimes[index] = i;
        index--;
        if (index < 0) break;
      }
    }
    final long executionEndTime = System.currentTimeMillis();

    // Write to primes.txt
    File summary = new File(FILENAME);
    summary.createNewFile();
    FileWriter writer = new FileWriter(FILENAME);
    writer.write("Limit: " + LIMIT + "\n");
    writer.write("Total execution time: " + (executionEndTime - executionStartTime) + "ms\n");
    writer.write("Total number of primes found: " + numPrimesFound + "\n");
    writer.write("Sum of all primes found: " + sumOfPrimes + "\n");
    writer.write("Top ten maximum primes (in ascending order):\n");
    for (int i = 0; i < maxPrimes.length; i++) {
      writer.write(String.valueOf((i + 1) + ") " + maxPrimes[i]) + "\n");
    }
    writer.close();
  }
}

class PrimeThread extends Thread {
  private long limit;
  private Sieve sieve;

  public PrimeThread(Sieve sieve, int limit) {
    this.sieve = sieve;
    this.limit = limit;
  }

  @Override
  public void run() {
    // Sieve of Eratosthenes algorithm
    while (sieve.getValue() <= Math.sqrt(limit)) {
      long currentVal = sieve.getAndIncrement();
      if (sieve.getSieveValue((int) currentVal)) {
        for (long i = (long) Math.pow(currentVal, 2); i <= limit; i += currentVal) {
          sieve.setSieveToFalse((int) i);
        }
      }
    }
  }
}

// Shared counter/array
class Sieve {
  private long value;
  private boolean[] sieve;

  public Sieve(long value, long limit) {
    this.value = value;
    this.sieve = new boolean[(int) limit + 1];
    Arrays.fill(sieve, true);
    if (sieve.length > 2) sieve[1] = false;
    if (sieve.length > 1) sieve[0] = false;
  }

  public synchronized long getAndIncrement() {
    value++;
    return value;
  }
  public synchronized long getValue() {
    return this.value;
  }
  public void setSieveToFalse(int index) {
    sieve[index] = false;
  }
  public boolean getSieveValue(int index) {
    return sieve[index];
  }
  public boolean[] getSieveArray() {
    return sieve;
  }
}
