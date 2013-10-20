package liquidrepair.util;

import java.util.List;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.liquids.LiquidStack;

import mods.tinker.tconstruct.TConstruct;
import mods.tinker.tconstruct.library.crafting.CastingRecipe;
import mods.tinker.tconstruct.library.tools.ToolCore;

public class RepairHelper {
    public static boolean isTool(ItemStack possibleTool)
    {
        if(possibleTool == null) {
            return false;
        }

        if(possibleTool.getItem() instanceof ToolCore) {
            return true;
        }
        return false;
    }

    /**
     * Find the liquid corresponding to the possibleTool's head. Returns null
     * if there doesn't seem to be an appropriate liquid, for whatever reason.
     */
    public static LiquidStack findLiquidFor(ItemStack tool)
    {
        if(!isTool(tool)) { return null; }

        Item head = ((ToolCore) tool.getItem()).getHeadItem();
        if(head == null) {
            return null;
        }

        return findLiquidFor(head, tool.getItemDamage());
    }

    public static HashMap<HashStack, LiquidStack> liquidMap = new HashMap<HashStack, LiquidStack>();

    /**
     * Find the liquid the given toolPart can be cast from. Returns null if
     * there doesn't seem to be a molten metal the toolPart can be cast from.
     */
    @SuppressWarnings("unchecked")
    public static LiquidStack findLiquidFor(Item toolPart, int materialID) {
        if(toolPart == null) {
            return null;
        }

        HashStack toolPartStack = new HashStack(new ItemStack(toolPart, 1, materialID));

        if(liquidMap.containsKey(toolPartStack)) {
            return liquidMap.get(toolPartStack).copy();
        }

        List<CastingRecipe> recipes = (List<CastingRecipe>)
                                      TConstruct.tableCasting.getCastingRecipes();

        for(CastingRecipe recipe: recipes) {
            if(recipe.output.isItemEqual(toolPartStack.itemStack)) {
                liquidMap.put(toolPartStack, recipe.castingMetal);
                return recipe.castingMetal.copy();
            }
        }

        return null;
    }

    /**
     * Tells you specifically if the given tool is repairable using molten
     * metal.
     */
    public static boolean isRepairable(ItemStack tool)
    {
        if(!isTool(tool)) { return false; }

        NBTTagCompound tag = tool.getTagCompound().getCompoundTag("InfiTool");
        if(tag.getInteger("Damage") > 0 && !tag.hasKey("charge") && findLiquidFor(tool) != null) {
            return true;
        }

        return false;
    }

    /**
     * "How damaged is this?"
     * Gives you the ratio (damage/max_damage) for the given tool.
     */
    public static float getDamageRatio(ItemStack tool)
    {
        if(!isTool(tool) || !isRepairable(tool)) { return 0.0F; }

        NBTTagCompound tag = tool.getTagCompound().getCompoundTag("InfiTool");
        if(tag.getInteger("BaseDurability") == 0) { return 0.0F; }

        return ((float) tag.getInteger("Damage")) /
               ((float) tag.getInteger("BaseDurability"));
    }
}
