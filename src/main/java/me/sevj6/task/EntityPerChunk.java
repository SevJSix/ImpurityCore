package me.sevj6.task;

import me.sevj6.task.scheduler.ScheduledTask;
import me.sevj6.task.scheduler.TaskForce;
import me.sevj6.util.Data;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.*;

public class EntityPerChunk implements TaskForce, Data {

    HashMap<EntityType, Integer> map;

    public EntityPerChunk() {
        this.map = entityMap;
    }

    @ScheduledTask(delay = (20L * 30L))
    public void deleteEntities() {
        for (Chunk chunk : getChunks()) {
            for (Map.Entry<EntityType, Integer> entry : map.entrySet()) {
                removeAmount(entry.getKey(), entry.getValue(), chunk);
            }
        }
    }

    private ArrayList<Chunk> getChunks() {
        ArrayList<Chunk> chunks = new ArrayList<>();
        for (World world : Bukkit.getWorlds()) {
            chunks.addAll(Arrays.asList(world.getLoadedChunks()));
        }
        return chunks;
    }

    private void removeAmount(EntityType type, int maxAmount, Chunk chunk) {
        List<Entity> correctType = new ArrayList<>();
        Arrays.stream(chunk.getEntities()).filter(entity -> entity.getType() == type).forEach(correctType::add);
        int entityAmount = correctType.size();
        if (entityAmount <= maxAmount) return;
        List<Entity> sized = correctType.subList(0, entityAmount - maxAmount);
        sized.forEach(Entity::remove);
    }
}
