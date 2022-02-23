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

package uk.ac.sussex.gdsc.examples.jmh.core.utils;

import com.koloboke.collect.impl.hash.LHashIntSetFactoryImpl;
import com.koloboke.collect.impl.hash.QHashIntSetFactoryImpl;
import com.koloboke.collect.set.hash.HashIntSet;
import com.koloboke.collect.set.hash.HashIntSetFactory;
import com.koloboke.collect.set.hash.HashIntSets;
import gnu.trove.set.hash.TIntHashSet;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import java.util.Arrays;
import java.util.BitSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.apache.commons.rng.sampling.distribution.DiscreteUniformSampler;
import org.apache.commons.rng.simple.RandomSource;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import uk.ac.sussex.gdsc.core.utils.IndexSet;
import uk.ac.sussex.gdsc.core.utils.IndexSets;

/**
 * Executes benchmark to compare the performance of a set of integers.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms2048M", "-Xmx2048M"})
public class IntSetBenchmark {

  /**
   * Simple implementation to store elements in an array.
   */
  private static class ArraySet {
    private final int[] data;
    private int size;

    /**
     * Create an instance with the given capacity.
     *
     * @param capacity the capacity
     */
    ArraySet(int capacity) {
      data = new int[capacity];
    }

    /**
     * Adds the item.
     *
     * @param item the item
     * @return true if the set was modified by the operation
     */
    boolean add(int item) {
      for (int i = size; i-- != 0;) {
        if (data[i] == item) {
          return false;
        }
      }
      data[size++] = item;
      return true;
    }

    /**
     * Get the number of items.
     *
     * @return the number of items
     */
    int size() {
      return size;
    }

    /**
     * Clear the set.
     */
    void clear() {
      size = 0;
    }
  }

  /**
   * Define the set implementation, the initial set capacity, and the range of numbers to insert.
   */
  @State(Scope.Benchmark)
  public static class AddSource {
    /**
     * The initial capacity. -1 for the default, 0 for the count of insertions, else the capacity.
     */
    @Param({"-1", "0"})
    private int capacity;
    /** The count of insertions. */
    @Param({"10", "100", "1000"})
    private int count;
    /** The lower bound. */
    @Param({"0"})
    private int low;
    /** The higher bound. */
    @Param({"2048", "1073741824"})
    private int high;
    /** The method. */
    @Param({
        // "baseline",
        // "ArraySet",
        "TreeSet", "BitSet", "TIntHashSet", "IntOpenHashSet", "IntAVLTreeSet", "IntRBTreeSet",
        "LHashIntSet", "QHashIntSet", "IndexSet"})
    private String method;
    /** Option to cache the set. */
    @Param({"true", "false"})
    private boolean cache;

    /** The generator to supply the next value. */
    private IntSupplier gen;

    /**
     * @return the next value
     */
    public int next() {
      return gen.getAsInt();
    }

    /**
     * Create the function.
     */
    @Setup
    public void setup() {
      gen = createFunction(method);
    }

    /**
     * Creates the generator function.
     *
     * @param method the method
     * @return the generator
     */
    protected IntSupplier createFunction(String method) {
      assert low >= 0 : "Invalid low: " + low;
      assert high > low : "Invalid high: " + high;
      assert count >= 0 : "Invalid size: " + count;
      final int size = count;
      final int initial = capacity == 0 ? size : capacity;
      final IntSupplier gen = DiscreteUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(),
          low, high - 1)::sample;

      if ("baseline".equals(method)) {
        return () -> {
          int r = 0;
          for (int i = size; i-- > 0;) {
            if (gen.getAsInt() == 42) {
              r++;
            }
          }
          return r;
        };
      } else if ("ArraySet".equals(method)) {
        if (initial < 0) {
          throw new IllegalStateException("Method does not support resizing: " + method);
        }
        Supplier<ArraySet> factory;
        if (cache) {
          final ArraySet set = new ArraySet(count);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> new ArraySet(count);
        }
        return () -> {
          final ArraySet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("TreeSet".equals(method)) {
        Supplier<TreeSet<Integer>> factory;
        if (cache) {
          final TreeSet<Integer> set = new TreeSet<>();
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> new TreeSet<>();
        }
        return () -> {
          final TreeSet<Integer> set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("BitSet".equals(method)) {
        Supplier<BitSet> factory;
        if (cache) {
          final BitSet set = initial < 0 ? new BitSet() : new BitSet(initial);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> initial < 0 ? new BitSet() : new BitSet(initial);
        }
        return () -> {
          final BitSet set = factory.get();
          int n = 0;
          for (int i = size; i-- > 0;) {
            final int v = gen.getAsInt();
            if (!set.get(v)) {
              set.set(v);
              n++;
            }
          }
          return n;
        };
      } else if ("TIntHashSet".equals(method)) {
        Supplier<TIntHashSet> factory;
        if (cache) {
          final TIntHashSet set = initial < 0 ? new TIntHashSet() : new TIntHashSet(initial);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> initial < 0 ? new TIntHashSet() : new TIntHashSet(initial);
        }
        return () -> {
          final TIntHashSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("IntOpenHashSet".equals(method)) {
        Supplier<IntOpenHashSet> factory;
        if (cache) {
          final IntOpenHashSet set =
              initial < 0 ? new IntOpenHashSet() : new IntOpenHashSet(initial);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> initial < 0 ? new IntOpenHashSet() : new IntOpenHashSet(initial);
        }
        return () -> {
          final IntOpenHashSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("IntAVLTreeSet".equals(method)) {
        Supplier<IntAVLTreeSet> factory;
        if (cache) {
          final IntAVLTreeSet set = new IntAVLTreeSet();
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> new IntAVLTreeSet();
        }
        return () -> {
          final IntAVLTreeSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("IntRBTreeSet".equals(method)) {
        Supplier<IntRBTreeSet> factory;
        if (cache) {
          final IntRBTreeSet set = new IntRBTreeSet();
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> new IntRBTreeSet();
        }
        return () -> {
          final IntRBTreeSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("LHashIntSet".equals(method)) {
        // Note: Updatable does not allow removal
        final HashIntSetFactory f = new LHashIntSetFactoryImpl().withKeysDomain(low, high - 1);
        Supplier<HashIntSet> factory;
        if (cache) {
          final HashIntSet set = initial < 0 ? f.newUpdatableSet() : f.newUpdatableSet(initial);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> initial < 0 ? f.newUpdatableSet() : f.newUpdatableSet(initial);
        }
        return () -> {
          final HashIntSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("QHashIntSet".equals(method)) {
        final HashIntSetFactory f = new QHashIntSetFactoryImpl().withKeysDomain(low, high - 1);
        Supplier<HashIntSet> factory;
        if (cache) {
          final HashIntSet set = initial < 0 ? f.newUpdatableSet() : f.newUpdatableSet(initial);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> initial < 0 ? f.newUpdatableSet() : f.newUpdatableSet(initial);
        }
        return () -> {
          final HashIntSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else if ("IndexSet".equals(method)) {
        Supplier<IndexSet> factory;
        if (cache) {
          final IndexSet set = initial < 0 ? IndexSets.create(10) : IndexSets.create(initial);
          factory = () -> {
            set.clear();
            return set;
          };
        } else {
          factory = () -> initial < 0 ? IndexSets.create(10) : IndexSets.create(initial);
        }
        return () -> {
          final IndexSet set = factory.get();
          for (int i = size; i-- > 0;) {
            set.add(gen.getAsInt());
          }
          return set.size();
        };
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
    }
  }

  /**
   * Define the set implementation and the set size for the for each iteration.
   */
  @State(Scope.Benchmark)
  public static class ForEachSource {
    /** The count of insertions. */
    @Param({"10", "100", "1000"})
    private int count;
    /** The lower bound. */
    @Param({"0"})
    private int low;
    /** The higher bound. */
    @Param({"2048", "1073741824"})
    private int high;
    /** The method. */
    @Param({
        // "baseline",
        "TreeSet", "BitSet", "TIntHashSet", "IntOpenHashSet", "IntAVLTreeSet", "IntRBTreeSet",
        "HashIntSet", "IndexSet"})
    private String method;
    /** Option to sort the indices. */
    @Param({"true", "false"})
    private boolean sorted;

    /** The generator to supply the next value. */
    private Consumer<Blackhole> consumer;

    /**
     * Perform an action for each item in the set.
     *
     * @param bh data sink (to consume the items)
     */
    public void forEach(Blackhole bh) {
      consumer.accept(bh);
    }

    /**
     * Create the function.
     */
    @Setup(Level.Iteration)
    public void setup() {
      consumer = createFunction(method);
    }

    /**
     * Creates the foreach consumer function.
     *
     * @param method the method
     * @return the consumer
     */
    protected Consumer<Blackhole> createFunction(String method) {
      assert low >= 0 : "Invalid low: " + low;
      assert high > low : "Invalid high: " + high;
      assert count >= 0 : "Invalid size: " + count;
      final IntSupplier gen = DiscreteUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(),
          low, high - 1)::sample;
      // Create distinct values
      final IndexSet values = IndexSets.create(10);
      while (values.size() < count) {
        values.put(gen.getAsInt());
      }

      if ("baseline".equals(method)) {
        final int size = count;
        return bh -> {
          for (int i = size; i-- > 0;) {
            bh.consume(i);
          }
        };
      } else if ("TreeSet".equals(method)) {
        final TreeSet<Integer> set = new TreeSet<>();
        values.forEach(set::add);
        return bh -> set.forEach(i -> bh.consume(i.intValue()));
      } else if ("BitSet".equals(method)) {
        final BitSet set = new BitSet();
        IntStream.generate(gen).limit(count).forEach(set::set);
        return bh -> {
          for (int i = set.nextSetBit(0); i >= 0; i = set.nextSetBit(i + 1)) {
            bh.consume(i);
            // Ignore for this benchmark
            // if (i == Integer.MAX_VALUE) {
            // break;
            // }
          }
        };
      } else if ("TIntHashSet".equals(method)) {
        final TIntHashSet set = new TIntHashSet();
        values.forEach(set::add);
        if (sorted) {
          final int[] a = new int[set.size()];
          return bh -> {
            set.toArray(a);
            Arrays.sort(a);
            for (final int i : a) {
              bh.consume(i);
            }
          };
        }
        return bh -> set.forEach(i -> {
          bh.consume(i);
          return true;
        });
      } else if ("IntOpenHashSet".equals(method)) {
        final IntOpenHashSet set = new IntOpenHashSet();
        values.forEach(set::add);
        if (sorted) {
          final int[] a = new int[set.size()];
          return bh -> {
            // This may not be very fast as it uses an iterator to fill the array
            set.toArray(a);
            Arrays.sort(a);
            for (final int i : a) {
              bh.consume(i);
            }
          };
        }
        return bh -> set.forEach(bh::consume);
      } else if ("IntAVLTreeSet".equals(method)) {
        final IntAVLTreeSet set = new IntAVLTreeSet();
        values.forEach(set::add);
        return bh -> set.forEach(bh::consume);
      } else if ("IntRBTreeSet".equals(method)) {
        final IntRBTreeSet set = new IntRBTreeSet();
        values.forEach(set::add);
        return bh -> set.forEach(bh::consume);
      } else if ("HashIntSet".equals(method)) {
        final HashIntSet set =
            HashIntSets.getDefaultFactory().withKeysDomain(low, high - 1).newUpdatableSet();
        values.forEach(set::add);
        if (sorted) {
          final int[] a = new int[set.size()];
          return bh -> {
            set.toArray(a);
            Arrays.sort(a);
            for (final int i : a) {
              bh.consume(i);
            }
          };
        }
        return bh -> set.forEach((IntConsumer) bh::consume);
      } else if ("IndexSet".equals(method)) {
        // Reuse the values as the set
        final IndexSet set = values;
        if (sorted) {
          final int[] a = new int[set.size()];
          return bh -> {
            set.toArray(a);
            Arrays.sort(a);
            for (final int i : a) {
              bh.consume(i);
            }
          };
        }
        return bh -> set.forEach((IntConsumer) bh::consume);
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
    }
  }

  /**
   * Add values to the set.
   *
   * @param source source of the set
   * @return the set size
   */
  @Benchmark
  public int add(AddSource source) {
    return source.next();
  }

  /**
   * Iterate entries of the set.
   *
   * @param source source of the set
   * @param bh data sink
   */
  @Benchmark
  public void forEach(ForEachSource source, Blackhole bh) {
    source.forEach(bh);
  }
}
