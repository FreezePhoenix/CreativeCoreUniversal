package team.creative.creativecore.common.util.ingredient;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CreativeIngredientBlockTag extends CreativeIngredient {
    
    public TagKey<Block> tag;
    
    public CreativeIngredientBlockTag(TagKey<Block> tag) {
        this.tag = tag;
    }
    
    public CreativeIngredientBlockTag() {
        
    }
    
    @Override
    protected void saveExtra(CompoundTag nbt) {
        nbt.putString("tag", tag.location().toString());
    }
    
    @Override
    protected void loadExtra(CompoundTag nbt) {
        tag = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(nbt.getString("tag")));
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean is(ItemStack stack) {
        Block block = Block.byItem(stack.getItem());
        if (block != null)
            return block.builtInRegistryHolder().is(tag);
        return false;
    }
    
    @Override
    public boolean is(CreativeIngredient info) {
        return info instanceof CreativeIngredientBlockTag && ((CreativeIngredientBlockTag) info).tag == tag;
    }
    
    @Override
    public ItemStack getExample() {
        var entry = Registry.BLOCK.getTag(tag).get();
        if (entry.size() == 0)
            return ItemStack.EMPTY;
        return new ItemStack(entry.get(0).value());
    }
    
    @Override
    public boolean equals(CreativeIngredient object) {
        return object instanceof CreativeIngredientBlockTag && ((CreativeIngredientBlockTag) object).tag == tag;
    }
    
    @Override
    public CreativeIngredient copy() {
        return new CreativeIngredientBlockTag(tag);
    }
    
}
