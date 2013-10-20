package liquidrepair.util;

import net.minecraft.item.ItemStack;

public class HashStack {
    public ItemStack itemStack;

    public HashStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public int hashCode() {
        if(itemStack == null) { return 0; }
        return (itemStack.getItemDamage() << 15) + itemStack.itemID;
    }

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof HashStack) || itemStack == null) {
            return false;
        }
        return itemStack.isItemEqual(((HashStack) other).itemStack);
    }
}
