//
// React - a library for functional-reactive-like programming in Java
// Copyright (c) 2011, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/react/blob/master/LICENSE

package react;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides utility methods for {@link Value}s.
 */
public class Values
{
    /**
     * Creates a boolean value that is toggled every time the supplied signal fires.
     *
     * @param signal the signal that will trigger the toggling.
     * @param initial the initial value of the to be toggled value.
     */
    public static ValueView<Boolean> toggler (SignalView<?> signal, boolean initial) {
        final Value<Boolean> value = Value.create(initial);
        signal.connect(new UnitSlot() {
            @Override public void onEmit () {
                value.update(!value.get());
            }
        });
        return value;
    }

    /**
     * Returns a value which is the logical NOT of the supplied value.
     */
    public static MappedValueView<Boolean> not (ValueView<Boolean> value) {
        return value.map(Functions.NOT);
    }

    /**
     * Returns a value which is the logical AND of the supplied values.
     */
    public static MappedValueView<Boolean> and (ValueView<Boolean>... values) {
        return and(Arrays.asList(values));
    }

    /**
     * Returns a value which is the logical AND of the supplied values.
     */
    public static MappedValueView<Boolean> and (final Iterable<ValueView<Boolean>> values) {
        return aggValue(values, COMPUTE_AND);
    }

    /**
     * Returns a value which is the logical OR of the supplied values.
     */
    public static MappedValueView<Boolean> or (ValueView<Boolean>... values) {
        return or(Arrays.asList(values));
    }

    /**
     * Returns a value which is the logical OR of the supplied values.
     */
    public static MappedValueView<Boolean> or (final Iterable<ValueView<Boolean>> values) {
        return aggValue(values, COMPUTE_OR);
    }

    protected static final MappedValueView<Boolean> aggValue (
        final Iterable<ValueView<Boolean>> values,
        final Function<Iterable<ValueView<Boolean>>,Boolean> aggOp) {
        final MappedValue<Boolean> mapped = new MappedValue<Boolean>() {
            @Override public Boolean get () {
                return aggOp.apply(values);
            }
        };
        final List<Connection> conns = new ArrayList<Connection>();
        final UnitSlot trigger = new UnitSlot() {
            public void onEmit () {
                boolean ovalue = _current;
                _current = aggOp.apply(values);
                mapped.notifyChange(_current, ovalue);
            }
            protected boolean _current = aggOp.apply(values);
        };
        for (ValueView<Boolean> value : values) {
            conns.add(value.connect(trigger));
        }
        mapped.setConnection(new Connection() {
            public void disconnect () {
                for (Connection conn : conns) conn.disconnect();
            }
            public Connection once () {
                for (Connection conn : conns) conn.once();
                return this;
            }
        });
        return mapped;
    }

    protected static final Function<Iterable<ValueView<Boolean>>,Boolean> COMPUTE_AND =
        new Function<Iterable<ValueView<Boolean>>,Boolean>() {
            public Boolean apply (Iterable<ValueView<Boolean>> values) {
                for (ValueView<Boolean> value : values) {
                    if (!value.get()) return false;
                }
                return true;
            }
        };

    protected static final Function<Iterable<ValueView<Boolean>>,Boolean> COMPUTE_OR =
        new Function<Iterable<ValueView<Boolean>>,Boolean>() {
            public Boolean apply (Iterable<ValueView<Boolean>> values) {
                for (ValueView<Boolean> value : values) {
                    if (value.get()) return true;
                }
                return false;
            }
        };

    private Values () {} // no constructski
}
