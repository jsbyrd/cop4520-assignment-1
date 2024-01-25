# Assignment 1 for COP4520
### Compilation Instructions:
1) Ensure you have Java 17 or higher installed
2) Download the code and locate PrimeNumbers.java in your terminal
3) To compile, run the command
    > javac PrimeNumbers.java
4) After compilation, you can execute the code by running
    > java PrimeNumbers
5) This program will output a file called `primes.txt`, which will display the limit you used (set to 100 million by
default), execution time, number of primes found, sum of all primes, and the 10 largest primes found.
6) If you want to change the value of n, simply change the variable named `LIMIT` that is located towards the start of
PrimeNumbers.java
### Code Explanation
I used the Sieve of Eratosthenes algorithm with multithreading in order to find all prime numbers between 1 and n. The
idea behind of the Sieve of Eratosthenes is to iterate from 2 to sqrt n. For each number n, find every multiple of n
and mark those numbers as not being prime. Anything that hasn't been marked must therefore be prime.
  
There are 8 threads being utilized, where each thread is responsible for computing every multiple for its given number.
When it is done finding every multiple for its number, it will pick the next number not already being worked on. The
threads will continue this process until every number from 2 to sqrt n has this done. Mutual exclusion is ensured
with a synchronized method called `getAndIncrement`, which ensures that no two  threads work on the same number. From
my brief experiments, I noticed that the utilization of 8 threads resulted in execution times that were 2-3 times faster
that those using a single thread.