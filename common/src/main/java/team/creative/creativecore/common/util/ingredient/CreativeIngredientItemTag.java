package team.creative.creativecore.common.util.ingredient;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CreativeIngredientItemTag extends CreativeIngredient {
    
    public TagKey<Item> tag;
    
    public CreativeIngredientItemTag(TagKey<Item> tag) {
        this.tag = tag;
    }
    
    public CreativeIngredientItemTag() {}
    
    @Override
    protected void saveExtra(CompoundTag nbt) {
        nbt.putString("tag", tag.location().toString());
    }
    
    @Override
    protected void loadExtra(CompoundTag nbt) {
        tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(nbt.getString("tag")));
    }
    
    @Override
    public boolean is(ItemStack stack) {
        return stack.is(tag);
    }
    
    @Override
    public boolean is(CreativeIngredient info) {
        return info instanceof CreativeIngredientItemTag && ((CreativeIngredientItemTag) info).tag == tag;
    }
    
    @Override
    public ItemStack getExample() {
        var entry = Registry.ITEM.getTag(tag).get();
        if (entry.size() == 0) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(entry.get(0));
    }
    
    @Override
    public boolean equals(CreativeIngredient object) {
        return object instanceof CreativeIngredientItemTag && ((CreativeIngredientItemTag) object).tag == tag;
    }
    
    @Override
    public CreativeIngredient copy() {
        return new CreativeIngredientItemTag(tag);
    }
}
