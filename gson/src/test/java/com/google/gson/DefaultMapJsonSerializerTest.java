/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;
import com.google.common.collect.Multimap;
import com.google.common.collect.ArrayListMultimap;

/**
 * Unit test for the default JSON map serialization object located in the
 * {@link DefaultTypeAdapters} class.
 *
 * @author Joel Leitch
 */
public class DefaultMapJsonSerializerTest extends TestCase {
  private Gson gson = new Gson();

  public void testEmptyMapNoTypeSerialization() {
    Map<String, String> emptyMap = new HashMap<String, String>();
    JsonElement element = gson.toJsonTree(emptyMap, emptyMap.getClass());
    assertTrue(element instanceof JsonObject);
    JsonObject emptyMapJsonObject = (JsonObject) element;
    assertTrue(emptyMapJsonObject.entrySet().isEmpty());
  }

  public void testEmptyMapSerialization() {
    Type mapType = new TypeToken<Map<String, String>>() { }.getType();
    Map<String, String> emptyMap = new HashMap<String, String>();
    JsonElement element = gson.toJsonTree(emptyMap, mapType);

    assertTrue(element instanceof JsonObject);
    JsonObject emptyMapJsonObject = (JsonObject) element;
    assertTrue(emptyMapJsonObject.entrySet().isEmpty());
  }

  public void testNonEmptyMapSerialization() {
    Type mapType = new TypeToken<Map<String, String>>() { }.getType();
    Map<String, String> myMap = new HashMap<String, String>();
    String key = "key1";
    myMap.put(key, "value1");
    Gson gson = new Gson();
    JsonElement element = gson.toJsonTree(myMap, mapType);

    assertTrue(element.isJsonObject());
    JsonObject mapJsonObject = element.getAsJsonObject();
    assertTrue(mapJsonObject.has(key));
  }
  

	/**
	 * Unit tests for Multimap serialization 
	 * @link https://github.com/google/guava/wiki/NewCollectionTypesExplained#multimap
	 * This is for the functionality of https://github.com/google/gson/issues/1847
	 * 
	 * @author Kathryn_DeWitt 
	 * CS427
	 */
  public void testEmptyMultiMapNoTypeSerialization() {
    Multimap<String, String> emptyMap = ArrayListMultimap.create();
    JsonElement element = gson.toJsonTree(emptyMap, emptyMap.getClass());
    assertTrue(element instanceof JsonObject);
    JsonObject emptyMapJsonObject = (JsonObject) element;
    assertTrue(emptyMapJsonObject.entrySet().isEmpty());
  }

  public void testEmptyMultiMapSerialization() {
    Type mapType = new TypeToken<ArrayListMultimap<String, String>>() { }.getType();
    Multimap<String, String> emptyMap =  ArrayListMultimap.create();
    JsonElement element = gson.toJsonTree(emptyMap, mapType);

    assertTrue(element instanceof JsonObject);
    JsonObject emptyMapJsonObject = (JsonObject) element;
    assertTrue(emptyMapJsonObject.entrySet().isEmpty());
  }

  public void testNonEmptyMultiMapSerialization() {
    Type mapType = new TypeToken<ArrayListMultimap<String, String>>() { }.getType();
    Multimap<String, String> myMap =  ArrayListMultimap.create();
    String key = "key1";
    myMap.put(key, "value1");
    Gson gson = new Gson();
    JsonElement element = gson.toJsonTree(myMap, mapType);

    //Verify that we get a JSON object with our key
    assertTrue(element.isJsonObject());
    JsonObject mapJsonObject = element.getAsJsonObject();
    assertTrue(mapJsonObject.has(key));
    
    //Confirm functionality matches a map
    JsonElement elementAsMap = gson.toJsonTree(myMap.asMap(), myMap.asMap().getClass());
    assertEquals(element, elementAsMap);
  }
  

  public void testDupKeyMultiMapSerialization() {
    Type mapType = new TypeToken<ArrayListMultimap<String, String>>() { }.getType();
    Multimap<String, String> myMap =  ArrayListMultimap.create();
    String key1 = "key1";
    String key2 = "key2";
    myMap.put(key1, "value1");
    myMap.put(key1,  "value2");
    myMap.put(key1,  "value3");
    myMap.put(key2,  "valueA");
    Gson gson = new Gson();
    JsonElement element = gson.toJsonTree(myMap, mapType);

    //Verify that we get a JSON object with our key
    assertTrue(element.isJsonObject());
    JsonObject mapJsonObject = element.getAsJsonObject();
    assertTrue(mapJsonObject.has(key1));
    assertTrue(mapJsonObject.has(key2));
    
    //Confirm functionality matches a map
    JsonElement elementAsMap = gson.toJsonTree(myMap.asMap(), myMap.asMap().getClass());
    assertEquals(element, elementAsMap);
  }
}
