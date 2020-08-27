package me.skiincraft.discord.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import me.skiincraft.discord.core.plugin.Plugin;
import me.skiincraft.discord.core.plugin.PluginManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventManager {

	private static List<Listener> listeners = new ArrayList<>();
	private static List<ListenerAdapter> JDAListeners = new ArrayList<>();
	
	public void callEvent(Event event) {
		Thread thread = new Thread(() -> {
			call(event);
		}, event.getClass().getSimpleName());
		thread.start();
	}
	
	public List<ListenerAdapter> getListenerAdapters(){
		return JDAListeners;
	}
	
	protected List<Listener> getListeners(){
		return listeners;
	}
	
	public void registerListener(Listener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void unregisterListener(Listener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}		
	}
	
	public void registerListener(ListenerAdapter listener) {
		if (!JDAListeners.contains(listener)) {
			JDAListeners.add(listener);
		}
	}
	
	public Plugin getPlugin() {
		return PluginManager.getPluginManager().getPlugin();
	}
	
	private void call(Event event) {
		List<EventRunnable> runPriority = new ArrayList<>();
		listeners.forEach(listener -> {
			Method[] methods = listener.getClass().getDeclaredMethods();
			for (Method method : methods) {
				// Getting all event by Annotation
				if (!method.isAnnotationPresent(EventTarget.class)) {
					continue;
				}
				// Discard incorrect methods.
				if (method.getParameters().length == 0 || method.getParameters().length >= 2) {
					continue;
				}
				// Check if method parameter extends Event
				if (method.getParameterTypes()[0] != event.getClass()) {
					continue;
				}
				//I create custom runnable from this.
				runPriority.add(new EventRunnable(method.getAnnotation(EventTarget.class).priority()) {
					public void run() {
						try {
							// Invoke method from event
							method.invoke(listener, event);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
		
		if (runPriority.size() == 0) {
			return;
		}
		
		//Sort for run by priority
		runPriority.sort(new Comparator<EventRunnable>() {
			public int compare(EventRunnable o1, EventRunnable o2) {
				return Integer.compare(o1.priority.getIntensity(), o2.priority.getIntensity());
			}
		});
		
		//call and run all methods.
		for (EventRunnable runnable : runPriority) {
			runnable.run();
		}
	}
	

	abstract class EventRunnable {
		private EventPriority priority;

		public EventRunnable(EventPriority priority) {
			this.priority = priority;
		}

		public abstract void run();

		public EventPriority getPriority() {
			return priority;
		}

		public void setPriority(EventPriority priority) {
			this.priority = priority;
		}
	}
	
	

}
