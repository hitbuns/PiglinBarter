package com.ConquestTech.Utilities;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.stream.Collectors;

public class ItemBuilder {

    ItemStack itemStack;
    ItemMeta meta;
    Map<String,Object> map = new HashMap<>();
    int customModelData = -1;

    public ItemBuilder customModelData(int modelData) {

        this.customModelData = modelData;

        return this;
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material,amount));
    }

    public ItemBuilder() {} //Inheritance purposes

    public void setup( ItemStack itemStack) {
        this.meta = (this.itemStack = itemStack).getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        setup(itemStack);
    }

    public ItemBuilder(Material material) {
        this(material,1);
    }

    public ItemBuilder setType(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder addModifiers(AttributeTypedModifier... attributeTypedModifiers) {
        for (AttributeTypedModifier attributeTypedModifier : attributeTypedModifiers) {
            meta.addAttributeModifier(attributeTypedModifier
                    .attribute,new AttributeModifier(UUID.randomUUID(),
                    "modifier_"+UUID.randomUUID()+"_"+Utils.RNGInt(0,10000),
                    attributeTypedModifier.value,attributeTypedModifier.operation,attributeTypedModifier
                    .equipmentSlot));
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(Utils.color(lore));
        return this;
    }

    public ItemBuilder color(Color color) {
        if (meta instanceof LeatherArmorMeta armorMeta)
            armorMeta.setColor(color);
        return this;
    }

    public ItemBuilder color(int red,int green,int blue) {
        return color(Color.fromRGB(colorBound(red),
                colorBound(green),
                colorBound(blue)));
    }

    public ItemBuilder skullMeta(OfflinePlayer offlinePlayer) {
        if (meta instanceof SkullMeta meta) meta.setOwningPlayer(offlinePlayer);
        return this;
    }

    int colorBound(int value) {
        return Math.max(0,Math.min(255,value));
    }

    public ItemBuilder setDisplayName(String name) {
        meta.setDisplayName(Utils.color(name));
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(Math.max(1,amount));
        return this;
    }

    public ItemBuilder nbt(String path,Object value) {
        this.map.put(path,value);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment,int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder clone() {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        itemBuilder.meta = this.meta.clone();
        itemBuilder.itemStack = this.itemStack.clone();
        return itemBuilder;
    }

    public ItemStack build(boolean clone) {

        itemStack.setItemMeta(meta);

        map.forEach((s, o) -> itemStack = setTag(new NBTItem(itemStack),s,o));

        if (customModelData > 0) {
            NBTItem nbtItem = new NBTItem(itemStack);
            nbtItem.setInteger("CustomModelData",customModelData);
            return clone ? nbtItem.getItem().clone() : nbtItem.getItem();
        }

        return clone ? itemStack.clone() : itemStack;
    }

    ItemStack setTag(NBTItem nbtCompound,String key, Object nbtBase) {
        if (key == null) return nbtCompound.getItem();
        int lead,tail = 0;
        NBTCompound nbtTagCompound = nbtCompound;
        while ((lead = key.indexOf(".",tail)) != -1) {
            String subCat = key.substring(tail,lead);
            tail = lead+1;
            if (key.indexOf(".",tail) == -1) {
                nbtTagCompound.setObject(subCat,nbtBase);
                return nbtCompound.getItem();
            }
            nbtTagCompound = nbtTagCompound.getOrCreateCompound(subCat);
        }
        return nbtCompound.getItem();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack( ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public ItemBuilder addFlags(ItemFlag... itemFlags) {
        getMeta().addItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... itemFlags) {
        getMeta().removeItemFlags(itemFlags);
        return this;
    }

    public ItemBuilder addLore(String... strings) {
        return addLore(meta.getLore().size()-1,strings);
    }

    public ItemBuilder addLore(int index,String... strings) {
        List<String> lore = this.meta.getLore();
        lore.addAll(index,Arrays.stream(strings).filter(Objects::nonNull)
                .map(Utils::color).collect(Collectors.toList()));
        meta.setLore(lore);
        return this;
    }

//    public ItemBuilder setGlowing(boolean b) {
//        if (!b) meta.removeEnchant(GlowEnchantment.getInstance());
//        else meta.addEnchant(GlowEnchantment.getInstance(),1,true);
//        return this;
//    }

    public record AttributeTypedModifier(Attribute attribute, AttributeModifier.Operation operation, double value,
                                         EquipmentSlot equipmentSlot) {

    }

    public static class AttributeBuilder {

        private final List<AttributeTypedModifier> attributeTypedModifiers = new ArrayList<>();

        public static AttributeBuilder init() {
            return new AttributeBuilder();
        }

        public AttributeBuilder addModifier(Attribute attribute, AttributeModifier.Operation operation,
                                 double value,EquipmentSlot equipmentSlot) {
            this.attributeTypedModifiers.add(new AttributeTypedModifier(attribute,
                    operation,value,equipmentSlot));
            return this;
        }

        public AttributeTypedModifier[] build() {
            return attributeTypedModifiers.toArray(AttributeTypedModifier[]::new);
        }


    }

}
