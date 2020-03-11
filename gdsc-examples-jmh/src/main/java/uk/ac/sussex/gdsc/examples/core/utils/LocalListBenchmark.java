/*-
 * #%L
 * Code for running JMH benchmarks to assess performance.
 * %%
 * Copyright (C) 2018 - 2020 Alex Herbert
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

package uk.ac.sussex.gdsc.examples.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import uk.ac.sussex.gdsc.core.utils.LocalList;
import uk.ac.sussex.gdsc.core.utils.MathUtils;

/**
 * Executes benchmark to compare the speed of List operations.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms128M", "-Xmx128M"})
public class LocalListBenchmark {

  /**
   * The source data size.
   */
  @State(Scope.Benchmark)
  public static class SourceSize {
    /** The size. */
    @Param({"10000"})
    private int size;

    /** The scratch space. */
    private Object[] scratch;

    /**
     * Gets the size.
     *
     * @return the size
     */
    public int getSize() {
      return size;
    }

    /**
     * Gets the scratch space.
     *
     * @return the scratch space
     */
    public Object[] getScratchSpace() {
      return scratch;
    }

    /**
     * Create the scratch space.
     */
    @Setup
    public void setup() {
      scratch = new Object[size];
    }
  }

  /**
   * The source data.
   */
  @State(Scope.Benchmark)
  public static class Source extends SourceSize {
    /** The order of the data. */
    @Param({"natural", "random"})
    private String order;

    /** The list. */
    @Param({"List", "LocalList"})
    private String list;

    /** Flag indicating the collector is for a local list. */
    private boolean isLocalList;

    /** The data. */
    private List<Integer> data;

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List<Integer> getData() {
      return data;
    }

    /**
     * Return a collector for the specified list.
     *
     * @param <T> the generic type
     * @return the collector
     */
    public <T> Collector<T, ?, List<T>> toList() {
      return isLocalList ? toLocalList() : Collectors.toList();
    }

    /**
     * Creates a new list.
     *
     * @param <T> the generic type
     * @return the list
     */
    public <T> List<T> createList() {
      return isLocalList ? new LocalList<>() : new ArrayList<>();
    }

    /**
     * Creates a new list with the given capacity.
     *
     * @param <T> the generic type
     * @param capacity the capacity
     * @return the list
     */
    public <T> List<T> createList(int capacity) {
      return isLocalList ? new LocalList<>(capacity) : new ArrayList<>(capacity);
    }

    /**
     * Creates a new list from the collection.
     *
     * @param <T> the generic type
     * @param c the collection
     * @return the list
     */
    public <T> List<T> createList(Collection<T> c) {
      return isLocalList ? new LocalList<>(c) : new ArrayList<>(c);
    }

    /**
     * Create the data.
     */
    @Override
    @Setup
    public void setup() {
      super.setup();
      if ("random".equals(order)) {
        data = new SplittableRandom().ints(getSize()).boxed().collect(Collectors.toList());
      } else {
        data = IntStream.range(0, getSize()).boxed().collect(Collectors.toList());
      }
      isLocalList = "LocalList".equals(list);
      if (isLocalList) {
        data = new LocalList<>(data);
      } else {
        data = new ArrayList<>(data);
      }
    }
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code LocalList}
   * as a {@code List}.
   *
   * @param <T> the type of the input elements
   * @return a {@code Collector} which collects all the input elements into a {@code LocalList}, in
   *         encounter order
   */
  private static <T> Collector<T, ?, List<T>> toLocalList() {
    return new LocalListCollector<>();
  }

  /**
   * Implementation of a Collector that uses a LocalList. Copied from
   * uk.ac.sussex.gdsc.core.utils.LocalCollectors.LocalListCollector to remove the binding to
   * LocalList to enable testing against the JDK List collector.
   *
   * @param <T> the generic type
   */
  private static class LocalListCollector<T> implements Collector<T, List<T>, List<T>> {

    /** The Constant CHARACTERISTICS. */
    private static final Set<Collector.Characteristics> CHARACTERISTICS =
        Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    @Override
    public Supplier<List<T>> supplier() {
      return LocalList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
      return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
      return (a, b) -> {
        // Use specialised method for combining two local lists
        a.addAll(b);
        return a;
      };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
      // Nothing to do
      return a -> a;
    }

    @Override
    public Set<Characteristics> characteristics() {
      return CHARACTERISTICS;
    }
  }

  /**
   * Stream the source list, convert to a list of Double and collect it.
   *
   * @param source source of list
   * @return the list
   */
  @Benchmark
  public List<Double> streamToDoubleList(Source source) {
    return source.getData().stream().map(i -> i.doubleValue()).collect(source.toList());
  }

  /**
   * Stream the source list in parallel, convert to a list of Double and collect it.
   *
   * @param source source of list
   * @return the list
   */
  @Benchmark
  public List<Double> parallelStreamToDoubleList(Source source) {
    return source.getData().parallelStream().map(i -> i.doubleValue()).collect(source.toList());
  }

  /**
   * Copy the list and remove powers of 2.
   *
   * @param source source of the list
   * @return the list
   */
  @Benchmark
  public List<Integer> copyAndRemovePowersOf2(Source source) {
    final List<Integer> list = source.createList(source.getData());
    list.removeIf(MathUtils::isPow2);
    return list;
  }

  /**
   * Iterate it to sum the contents.
   *
   * @param source source of the list
   * @return the sum
   */
  @Benchmark
  public long sumUsingIterator(Source source) {
    long sum = 0;
    for (final Integer i : source.createList(source.getData())) {
      sum += i;
    }
    return sum;
  }

  /**
   * Use the get method to access the contents.
   *
   * @param source source of the list
   * @return the contents
   */
  @Benchmark
  public Object[] get(Source source) {
    final List<Integer> ll = source.getData();
    final Object[] scratch = source.getScratchSpace();
    for (int i = 0; i < ll.size(); i++) {
      scratch[i] = ll.get(i);
    }
    return scratch;
  }

  /**
   * Use the get method to sum the contents.
   *
   * @param source source of the list
   * @return the sum
   */
  @Benchmark
  public long sumUsingGet(Source source) {
    long sum = 0;
    final List<Integer> ll = source.getData();
    for (int i = 0; i < ll.size(); i++) {
      sum += ll.get(i);
    }
    return sum;
  }

  /**
   * Use the unsafe get method to sum the contents.
   *
   * @param source source of the list
   * @return the sum
   */
  @Benchmark
  public long sumUsingUnsafeGet(Source source) {
    long sum = 0;
    if (source.isLocalList) {
      final LocalList<Integer> ll = (LocalList<Integer>) source.getData();
      for (int i = 0; i < ll.size(); i++) {
        sum += ll.unsafeGet(i);
      }
    } else {
      final List<Integer> ll = source.getData();
      for (int i = 0; i < ll.size(); i++) {
        sum += ll.get(i);
      }
    }
    return sum;
  }

  /**
   * Use the add method to fill the contents.
   *
   * @param source source of the list
   * @return the list
   */
  @Benchmark
  public List<Object> fillUsingAdd(Source source) {
    final List<Object> ll = source.createList();
    for (int i = source.getSize(); i-- > 0;) {
      ll.add(ll);
    }
    return ll;
  }

  /**
   * Use the add method to fill the contents.
   *
   * @param source source of the list
   * @return the list
   */
  @Benchmark
  public List<Object> fillCapacityUsingAdd(Source source) {
    final List<Object> ll = source.createList(source.getSize());
    for (int i = source.getSize(); i-- > 0;) {
      ll.add(ll);
    }
    return ll;
  }

  /**
   * Use the add method to fill the contents.
   *
   * @param source source of the list
   * @return the list
   */
  @Benchmark
  public List<Object> fillCapacityUsingPush(SourceSize source) {
    final LocalList<Object> ll = new LocalList<>(source.getSize());
    for (int i = source.getSize(); i-- > 0;) {
      ll.push(ll);
    }
    return ll;
  }

  /**
   * Perform a shuffle.
   *
   * @param source source of the list
   * @return the list
   */
  @Benchmark
  public List<Integer> shuffle(Source source) {
    final List<Integer> ll = source.getData();
    Collections.shuffle(ll, ThreadLocalRandom.current());
    return ll;
  }
}
