package me.sevj6.event.bus;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private final ConcurrentHashMap<SevListener, List<Method>> listeners = new ConcurrentHashMap<>();

    public void subscribe(SevListener listener) {
        listeners.put(listener, listener.getMethodsByPrio());
    }

    public void unSubscribe(SevListener listener) {
        listeners.remove(listener);
    }

    public synchronized void post(Event event) {
        listeners.forEach((listener, methods) -> methods.forEach(method -> {
            try {
                Class<?> param = method.getParameters()[0].getType();
                if (param.equals(event.getClass())) {
                    method.invoke(listener, event);
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }));
    }
}
