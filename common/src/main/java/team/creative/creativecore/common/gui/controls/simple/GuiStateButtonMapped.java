package team.creative.creativecore.common.gui.controls.simple;

import java.util.List;

import team.creative.creativecore.common.util.text.TextMapBuilder;

public class GuiStateButtonMapped<K> extends GuiStateButton {
    
    private List<K> keys;
    
    public GuiStateButtonMapped(String name, TextMapBuilder<K> lines) {
        super(name, lines);
        this.keys = lines.keys();
    }
    
    public K getSelected() {
        int index = getState();
        if (index < keys.size())
            return keys.get(index);
        return null;
    }
    
    public K getSelected(K defaultValue) {
        int index = getState();
        if (index < keys.size())
            return keys.get(index);
        return defaultValue;
    }
    
    public void select(K key) {
        int index = keys.indexOf(key);
        if (index != -1)
            setState(index);
    }
}
