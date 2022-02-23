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

import com.koloboke.collect.map.hash.HashIntIntMap;
import com.koloboke.collect.map.hash.HashIntIntMapFactory;
import com.koloboke.collect.map.hash.HashIntIntMaps;
import gnu.trove.map.hash.TIntIntHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
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
 * Executes benchmark to compare the performance of maps from integers to integers.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-server", "-Xms2048M", "-Xmx2048M"})
public class IntIntMapBenchmark {

  /**
   * Define the map implementation, the initial map capacity, and the range of numbers to insert.
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
        "HashSet", "TIntIntHashMap", "Int2IntOpenHashMap", "HashIntIntMap"})
    private String method;
    /** Option to cache the map. */
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
      } else if ("HashSet".equals(method)) {
        Supplier<HashMap<Integer, Integer>> factory;
        if (cache) {
          final HashMap<Integer, Integer> map = new HashMap<>();
          factory = () -> {
            map.clear();
            return map;
          };
        } else {
          factory = () -> new HashMap<>();
        }
        return () -> {
          final HashMap<Integer, Integer> map = factory.get();
          for (int i = size; i-- > 0;) {
            map.compute(gen.getAsInt(), (k, v) -> v == null ? 1 : v + 1);
          }
          return map.size();
        };
      } else if ("TIntIntHashMap".equals(method)) {
        Supplier<TIntIntHashMap> factory;
        if (cache) {
          final TIntIntHashMap map =
              initial < 0 ? new TIntIntHashMap() : new TIntIntHashMap(initial);
          factory = () -> {
            map.clear();
            return map;
          };
        } else {
          factory = () -> initial < 0 ? new TIntIntHashMap() : new TIntIntHashMap(initial);
        }
        return () -> {
          final TIntIntHashMap map = factory.get();
          for (int i = size; i-- > 0;) {
            map.adjustOrPutValue(gen.getAsInt(), 1, 1);
          }
          return map.size();
        };
      } else if ("Int2IntOpenHashMap".equals(method)) {
        Supplier<Int2IntOpenHashMap> factory;
        if (cache) {
          final Int2IntOpenHashMap map =
              initial < 0 ? new Int2IntOpenHashMap() : new Int2IntOpenHashMap(initial);
          factory = () -> {
            map.clear();
            return map;
          };
        } else {
          factory = () -> initial < 0 ? new Int2IntOpenHashMap() : new Int2IntOpenHashMap(initial);
        }
        return () -> {
          final Int2IntOpenHashMap map = factory.get();
          for (int i = size; i-- > 0;) {
            map.addTo(gen.getAsInt(), 1);
          }
          return map.size();
        };
      } else if ("HashIntIntMap".equals(method)) {
        final HashIntIntMapFactory f = HashIntIntMaps.getDefaultFactory().withKeysDomain(low, high - 1);
        Supplier<HashIntIntMap> factory;
        if (cache) {
          final HashIntIntMap map = initial < 0 ? f.newUpdatableMap() : f.newUpdatableMap(initial);
          factory = () -> {
            map.clear();
            return map;
          };
        } else {
          factory = () -> initial < 0 ? f.newUpdatableMap() : f.newUpdatableMap(initial);
        }
        return () -> {
          final HashIntIntMap map = factory.get();
          for (int i = size; i-- > 0;) {
            map.addValue(gen.getAsInt(), 1);
          }
          return map.size();
        };
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
    }
  }

  /**
   * Define the map implementation and the map size for the for each iteration.
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
        "HashMap", "TIntIntHashMap", "Int2IntOpenHashMap", "HashIntIntMap"})
    private String method;

    /** The generator to supply the next value. */
    private Consumer<Blackhole> consumer;

    /**
     * Perform an action for each item in the map.
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
      final IntSupplier gen =
          DiscreteUniformSampler.of(RandomSource.XO_RO_SHI_RO_128_PP.create(), low, high)::sample;
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
            bh.consume(~i);
          }
        };
      } else if ("HashMap".equals(method)) {
        final HashMap<Integer, Integer> map = new HashMap<>();
        values.forEach(k -> map.put(k, ~k));
        return bh -> map.forEach((k, v) -> {
          bh.consume(k.intValue());
          bh.consume(v.intValue());
        });
      } else if ("TIntIntHashMap".equals(method)) {
        final TIntIntHashMap map = new TIntIntHashMap();
        values.forEach(k -> map.put(k, ~k));
        return bh -> map.forEachEntry((k, v) -> {
          bh.consume(k);
          bh.consume(v);
          return true;
        });
      } else if ("Int2IntOpenHashMap".equals(method)) {
        final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
        values.forEach(k -> map.put(k, ~k));
        return bh -> map.forEach((k, v) -> {
          bh.consume(k);
          bh.consume(v);
        });
      } else if ("HashIntIntMap".equals(method)) {
        final HashIntIntMap map = HashIntIntMaps.newUpdatableMap();
        values.forEach(k -> map.put(k, ~k));
        return bh -> map.forEach((int k, int v) -> {
          bh.consume(k);
          bh.consume(v);
        });
      } else {
        throw new IllegalStateException("Unknown method: " + method);
      }
    }
  }

  /**
   * Add values to the map.
   *
   * @param source source of the map
   * @return the map size
   */
  @Benchmark
  public int add(AddSource source) {
    return source.next();
  }

  /**
   * Iterate entries of the map.
   *
   * @param source source of the map
   * @param bh data sink
   */
  @Benchmark
  public void forEach(ForEachSource source, Blackhole bh) {
    source.forEach(bh);
  }
}
