package com.wow.dev.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ExtractJSONDetails {
	private JSONParser parser;
	
	public ExtractJSONDetails() {
		parser = new JSONParser();
	}
	
	public Map<Object, Object> extractDataFromJSON(String parentNode,String jsonValue) {
		Map<Object,Object> objectDetails=new LinkedHashMap<Object,Object>();
		Set<Object> jsonKeySet=new HashSet<Object>();
		try {
			JSONObject jsonObject= (JSONObject) parser.parse(new StringReader(jsonValue));
			jsonKeySet=jsonObject.keySet();
			Iterator<Object> it=jsonKeySet.iterator();
			while(it.hasNext()) {
				Object key=it.next();
				if(jsonObject.get(key)!=null) {
					String classType=jsonObject.get(key).getClass().toString();
					if(classType.contentEquals("class org.json.simple.JSONObject")) {
						objectDetails.put(parentNode+"."+key.toString(),extractDataFromJSON(key.toString(),jsonObject.get(key).toString()));
					}else if(classType.contentEquals("class org.json.simple.JSONArray")){
						String arrayValue=jsonObject.get(key).toString();
						if(arrayValue.charAt(1)!='{') {
							objectDetails.put(parentNode+"."+key.toString(), arrayValue);
						}else {
							JSONArray jArray=(JSONArray) jsonObject.get(key);
							Object obj[]=jArray.toArray();
							for(int i=0;i<obj.length;i++) {
								objectDetails.put(parentNode+"."+key.toString()+"("+i+")", extractDataFromJSON(key.toString(), obj[i].toString()));
							}
						}
					}else {
						objectDetails.put(parentNode+"."+key.toString(), jsonObject.get(key).toString());
					}
				}else {
					objectDetails.put(parentNode+"."+key.toString(), "null");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return objectDetails;
	}
	
	public void printJSONDetails(Map<Object, Object> objectDetails) {
		Object[] o=objectDetails.keySet().toArray();
		for(int i=0;i<o.length;i++) {
			if(objectDetails.get(o[i]).getClass().toString().contentEquals("class java.util.LinkedHashMap")) {
				Map<Object,Object> contentDetails=(Map) objectDetails.get(o[i]);
				Object[] c=contentDetails.keySet().toArray();
				for(int j=0;j<c.length;j++) {
					System.out.println(c[j]+":"+contentDetails.get(c[j]));
				}
			}else {
				System.out.println(o[i]+":"+objectDetails.get(o[i]));
			}
			
			
		}
		System.out.println("----------------------------------");
	}
	
	public void printJSONDetails(Map<Object,Object> objectDetails,String nodeName) {
		Object[] o=objectDetails.keySet().toArray();
		for(int i=0;i<o.length;i++) {
			Map<Object,Object> contentDetails=(Map) objectDetails.get(o[i]);
			Object[] c=contentDetails.keySet().toArray();
			for(int j=0;j<c.length;j++) {
				if(c[j].toString().contains(nodeName)) {
					System.out.println(c[j]+":"+contentDetails.get(c[j]));
				}
			}
			System.out.println("----------------------------------");
		}
	}

	public int getChildCount(Map<Object,Object> objectDetails,String nodeName) {
		int count=0;
		Object[] o=objectDetails.keySet().toArray();
		for(int i=0;i<o.length;i++) {
			if(objectDetails.get(o[i]).getClass().toString().contentEquals("class java.util.LinkedHashMap")) {
				Map<Object,Object> contentDetails=(Map<Object,Object>) objectDetails.get(o[i]);
				Object[] c=contentDetails.keySet().toArray();
				for(int j=0;j<c.length;j++) {
					if(c[j].toString().contains(nodeName)) {
						count++;
					}
				}
			}else {
				// calculate count based on String values
			}
		}
		return count;
	}
	
	public int getParentCount(Map<Object,Object> objectDetails,String nodeName) {
		int count=0;
		Object[] o=objectDetails.keySet().toArray();
		for(int i=0;i<o.length;i++) {
			if(o[i].toString().contains(nodeName)) {
				count++;
			}
		}
		return count;
	}
	

}
