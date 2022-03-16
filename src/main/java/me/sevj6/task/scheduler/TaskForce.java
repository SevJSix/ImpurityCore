package me.sevj6.task.scheduler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface TaskForce {

    default List<Method> getMethods() {
        List<Method> methods = new ArrayList<>();
        for (Method method : this.getClass().getMethods()) {
            if (!method.isAccessible()) method.setAccessible(true);
            if (method.isAnnotationPresent(ScheduledTask.class)) {
                methods.add(method);
            }
        }
        return methods;
    }
}
