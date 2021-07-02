package cn.techection.mall.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JSONutils {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static <T> T fromJson(String Json,Class<T> clz){
        return gson.fromJson(Json,clz);
    }

    public static <T> T fromJson(String Json, Type type){
        return gson.fromJson(Json,type);
    }

    public static String toJson(Object obj){
        return gson.toJson(obj);
    }

    public static Gson getGson(){
        return gson;
    }
}
