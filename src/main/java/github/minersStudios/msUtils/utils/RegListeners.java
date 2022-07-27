package github.minersStudios.msUtils.utils;

import github.minersStudios.msUtils.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class RegListeners {

    public static void init(@Nonnull String path) {
        Set<Class<?>> listenerClasses = new Reflections((Object[]) new String[]{path}).getTypesAnnotatedWith(EventListener.class);
        listenerClasses.forEach((listener) -> {
            try {
                Bukkit.getPluginManager().registerEvents((Listener) listener.getConstructor().newInstance(), Main.plugin);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                exception.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(Main.plugin);
            }
        });
    }
}
