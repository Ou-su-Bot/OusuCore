package me.skiincraft.ousucore.common.reactions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Reactions {

    private static final List<Reactions> allChooser = new ArrayList<>();
    private final ReactionListeners listener;

    public Reactions(ReactionListeners listener){
        this.listener = listener;
        if (allChooser.stream().noneMatch(chooser -> chooser.getListener() == listener)){
            allChooser.add(this);
        }
    }

    public void registerReaction(ReactionObject object, Reaction reaction){
        listener.addElement(object, reaction);
    }

    public void removeReaction(ReactionObject object){
        listener.removeElement(object);
    }

    public static Reactions of(ReactionListeners listener){
        return allChooser.stream().filter(chooser -> chooser.getListener() == listener).findFirst().orElse(new Reactions(listener));
    }

    public static List<Reactions> of(ReactionObject object){
        return allChooser.stream().filter(chooser -> chooser.getListener().contains(object)).collect(Collectors.toList());
    }

    public static Reactions getInstance() {
        if (allChooser.size() == 0){
            return null;
        }
        return allChooser.get(0);
    }

    public ReactionListeners getListener() {
        return listener;
    }
}
