package me.skiincraft.ousucore.command.utils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ThreadConsumer<T> implements Consumer<T> {

    protected final AtomicReference<T> object = new AtomicReference<>();
    private final Thread mainThread;

    public ThreadConsumer() {
        this.mainThread = Thread.currentThread();
    }

    public ThreadConsumer(Thread mainThread) {
        this.mainThread = mainThread;
    }

    public void waitConsumer() {
        while (object.get() == null){
            if (object.get() != null) break;
        }
    }

    public Thread getMainThread() {
        return mainThread;
    }

    @Override
    public final void accept(T t) {
        object.set(t);
    }

    public T get(){
        return object.get();
    }
}
