package net.lordprinz.bloodharbourripper.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
        RecordCodecBuilder.create(inst -> codecStart(inst).and(
            inst.group(
                ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item),
                Codec.INT.fieldOf("min_count").forGetter(m -> m.minCount),
                Codec.INT.fieldOf("max_count").forGetter(m -> m.maxCount),
                Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)
            )
        ).apply(inst, AddItemModifier::new))
    );

    private final Item item;
    private final int minCount;
    private final int maxCount;
    private final float chance;

    protected AddItemModifier(LootItemCondition[] conditionsIn, Item item, int minCount, int maxCount, float chance) {
        super(conditionsIn);
        this.item = item;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.chance = chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (context.getRandom().nextFloat() < chance) {
            int count = minCount + context.getRandom().nextInt(maxCount - minCount + 1);
            generatedLoot.add(new ItemStack(item, count));
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}

