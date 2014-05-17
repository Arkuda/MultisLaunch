package net.feed_the_beast.launcher.json;

import java.lang.reflect.Type;

import com.google.common.collect.ForwardingMultimap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

public class OldPropertyMapSerializer implements JsonSerializer<PropertyMap> {

    @Override
    public JsonElement serialize (PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject out = new JsonObject();
        for (String key : ((ForwardingMultimap<String, Property>) src).keySet()) {
            JsonArray jsa = new JsonArray();
            for (Property p : ((ForwardingMultimap<String, Property>) src).get(key)) {
                jsa.add(new JsonPrimitive(p.getValue()));
            }
            out.add(key, jsa);
        }
        return out;
    }
}
