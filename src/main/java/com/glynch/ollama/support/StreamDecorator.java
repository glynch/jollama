package com.glynch.ollama.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamDecorator implements InvocationHandler {
    private final BaseStream<?, ?> delegate;
    private final Consumer<Runnable> leafDecorator;

    private StreamDecorator(final BaseStream<?, ?> delegate, final Consumer<Runnable> leafDecorator) {
        this.delegate = delegate;
        this.leafDecorator = leafDecorator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            final boolean stream = BaseStream.class.isAssignableFrom(method.getReturnType());
            final Object result = stream ? method.invoke(delegate, args) : wrap(() -> {
                try {
                    return method.invoke(delegate, args);
                } catch (final IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                } catch (final InvocationTargetException e) {
                    throw new IllegalArgumentException(e.getTargetException());
                }
            });
            if (stream) {
                if (Stream.class.isInstance(result)) {
                    return decorate(Stream.class.cast(result), Stream.class, leafDecorator);
                }
                if (IntStream.class.isInstance(result)) {
                    return decorate(IntStream.class.cast(result), IntStream.class, leafDecorator);
                }
                if (LongStream.class.isInstance(result)) {
                    return decorate(LongStream.class.cast(result), LongStream.class, leafDecorator);
                }
                if (DoubleStream.class.isInstance(result)) {
                    return decorate(DoubleStream.class.cast(result), DoubleStream.class, leafDecorator);
                }
            }
            return result;
        } catch (final InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    private <V> V wrap(final Supplier<V> supplier) {
        final AtomicReference<V> ref = new AtomicReference<>();
        leafDecorator.accept(() -> ref.set(supplier.get()));
        return ref.get();
    }

    @SuppressWarnings("unchecked")
    public static <T> Stream<T> decorate(final Stream<T> delegate, final Consumer<Runnable> wrapper) {
        return decorate(delegate, Stream.class, wrapper);
    }

    @SuppressWarnings("unchecked")
    private static <T, S extends BaseStream<T, ?>> S decorate(final S delegate, final Class<S> type,
            final Consumer<Runnable> wrapper) {
        return (S) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { type },
                new StreamDecorator(delegate, wrapper));
    }
}
