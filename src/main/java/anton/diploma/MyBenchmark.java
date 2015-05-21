/*
 * Copyright (c) 2014, Oracle America, Inc. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * * Neither the name of Oracle nor the names of its contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package anton.diploma;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import polynomial.Polynomial;
import polynomial.PolynomialSlow;

public class MyBenchmark {

  @State(Scope.Benchmark)
  public static class BenchmarkState {

    final BigInteger POWER = BigInteger.valueOf(17);

    Polynomial polynom1 = Polynomial.createRandom(512);

    Polynomial polynom2 = Polynomial.createRandom(512);

    Polynomial mod = Polynomial.createRandom(256);

    PolynomialSlow polynomSlow1 = new PolynomialSlow(polynom1.getDegrees());

    PolynomialSlow polynomSlow2 = new PolynomialSlow(polynom2.getDegrees());

    PolynomialSlow modSlow = new PolynomialSlow(mod.getDegrees());
  }

  @Benchmark
  public void creatingRandomPolynomial(Blackhole bh) {
    Polynomial poly = Polynomial.createRandom(512);
    bh.consume(poly);
  }

  @Benchmark
  public void addPolynomials(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.add(state.polynom2);
    bh.consume(poly);
  }

  @Benchmark
  public void multiplyPolynomials(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.multiply(state.polynom2);
    bh.consume(poly);
  }

  @Benchmark
  public void modPolynomials(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.mod(state.mod);
    bh.consume(poly);
  }

  @Benchmark
  public void andPolynomials(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.and(state.mod);
    bh.consume(poly);
  }

  @Benchmark
  public void orPolynomials(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.or(state.mod);
    bh.consume(poly);
  }

  @Benchmark
  public void isReduciblePolynomial(Blackhole bh, BenchmarkState state) {
    boolean reducible = state.polynom1.isReducible();
    bh.consume(reducible);
  }

  @Benchmark
  public void gcdPolynomilas(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.gcd(state.polynom2);
    bh.consume(poly);
  }

  @Benchmark
  public void powModPolynomilas(Blackhole bh, BenchmarkState state) {
    Polynomial poly = state.polynom1.modPow(state.POWER, state.mod);
    bh.consume(poly);
  }

  @Benchmark
  public void creatingRandomPolynomialSlow(Blackhole bh) {
    PolynomialSlow poly = PolynomialSlow.createRandom(512);
    bh.consume(poly);
  }

  @Benchmark
  public void addPolynomialsSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.add(state.polynomSlow2);
    bh.consume(poly);
  }

  @Benchmark
  public void multiplyPolynomialsSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.multiply(state.polynomSlow2);
    bh.consume(poly);
  }

  @Benchmark
  public void modPolynomialsSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.mod(state.modSlow);
    bh.consume(poly);
  }

  @Benchmark
  public void andPolynomialsSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.and(state.modSlow);
    bh.consume(poly);
  }

  @Benchmark
  public void orPolynomialsSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.or(state.modSlow);
    bh.consume(poly);
  }

  @Benchmark
  public void isReduciblePolynomialSlow(Blackhole bh, BenchmarkState state) {
    boolean reducible = state.polynomSlow1.isReducible();
    bh.consume(reducible);
  }

  @Benchmark
  public void gcdPolynomilasSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.gcd(state.polynomSlow2);
    bh.consume(poly);
  }

  @Benchmark
  public void powModPolynomilasSlow(Blackhole bh, BenchmarkState state) {
    PolynomialSlow poly = state.polynomSlow1.modPow(state.POWER, state.modSlow);
    bh.consume(poly);
  }

  // to run benchmark: java -cp target/benchmarks.jar anton.diploma.MyBenchmark
  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
    .include(MyBenchmark.class.getSimpleName())
    .warmupIterations(10)
    .measurementIterations(10)
    .forks(4)
    .mode(Mode.AverageTime)
    .timeUnit(TimeUnit.MICROSECONDS)
    .result("D:\\benchmarkResults.txt")
    .build();
    new Runner(opt).run();
  }
}
