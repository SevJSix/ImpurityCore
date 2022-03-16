package me.sevj6.task.scheduler;

import me.sevj6.Impurity;
import me.sevj6.task.AutoRestart;
import me.sevj6.task.EntityPerChunk;
import me.sevj6.task.TabList;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TaskManager {

    private final ConcurrentHashMap<TaskForce, List<Method>> tasks = new ConcurrentHashMap<>();

    public TaskManager(Impurity plugin) {
        registerTask(new TabList());
        registerTask(new EntityPerChunk());
        registerTask(new AutoRestart());
        tasks.forEach((task, methods) -> {
            methods.forEach(m -> {
                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                    try {
                        m.invoke(task);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }, 20L, m.getAnnotation(ScheduledTask.class).delay());
            });
        });
    }

    public void registerTask(TaskForce task) {
        tasks.put(task, task.getMethods());
    }
}
