package com.sinepulse.app.utils;

import java.lang.reflect.Type;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class DoubleDeserializer implements JsonDeserializer<Double> {
	@Override
	  public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	      throws JsonParseException {
//		Log.d("doubleparser", json.getAsJsonPrimitive().getAsString());
	    try{
	    	return Double.valueOf(json.getAsJsonPrimitive().getAsString());
	    }catch(NumberFormatException ex){
	    	String s = json.getAsJsonPrimitive().getAsString();
	    	//lets replace comma with dots
	    	s = s.replace(",", ".");
	    	try{
	    		return Double.valueOf(s);
	    	}catch(Exception ex2){
	    		return 0.0;
	    	}
//	    	return 0.0;
	    }
	  }

	
	}