package team.creative.creativecore.common.config.gui;

import net.minecraft.network.chat.TextComponent;
import team.creative.creativecore.common.gui.GuiParent;
import team.creative.creativecore.common.gui.controls.simple.GuiLabel;
import team.creative.creativecore.common.gui.controls.simple.GuiTextfield;
import team.creative.creativecore.common.gui.flow.GuiFlow;
import team.creative.creativecore.common.gui.style.ControlFormatting;

public class GuiConfigSubControl extends GuiParent {
    
    public GuiTextfield nameField;
    public GuiLabel nameLabel;
    
    public GuiConfigSubControl(String name) {
        super(name, GuiFlow.STACK_X);
        setExpandableX();
    }
    
    public String getName() {
        if (nameLabel != null)
            return nameLabel.name;
        if (nameField != null)
            return nameField.getText();
        return "";
    }
    
    public void addNameUnmodifieable(String name) {
        nameLabel = new GuiLabel(name).setTitle(new TextComponent(name));
        add(nameLabel);
    }
    
    public void addNameTextfield(String name) {
        nameField = new GuiTextfield(name, 50);
        add(nameField);
    }
    
    @Override
    public ControlFormatting getControlFormatting() {
        return ControlFormatting.TRANSPARENT;
    }
}
