package ontotext.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class test_original {
	
	public static HashMap<String, Set<String>> jsonHash (String nameOfJson) throws IOException, ParseException{
		JSONParser parser = new JSONParser();
		HashMap<String, Set<String>> pictures = new HashMap<>();
		String loshiDumichki[] = { "is", "and", "the", "a", "an", "i", "you",
				"do", "get", "am", "of", "me", "he", "she", "this", "that",
				"we", "they", "not", "its", "but", "will", "for", "here",
				"there", "from", "in", "or", "be", "my", "it", "was", "were",
				"on", "are", "to", "with", "it", "so", "each", "has", "have",
				"can", "them", "now", "him", "her", "at", "if", "have", "what",
				"who", "only", "also", "about", "no", "had", "as", "so",
				"just", "too", "when", "your", "think", "would" };

		try {
			Object obj = parser.parse(new FileReader(nameOfJson));

			JSONObject jsonObject = (JSONObject) obj;

			JSONObject album = (JSONObject) jsonObject.get("albums");
			JSONArray data = (JSONArray) album.get("data");
			// System.out.println("data: " + data);

			Iterator<JSONObject> iterator = data.iterator();
			while (iterator.hasNext()) {
				JSONObject alb = (JSONObject) iterator.next();

				JSONObject photos = (JSONObject) alb.get("photos");

				JSONArray dataPhotos = (JSONArray) photos.get("data");

				Iterator<JSONObject> iterator2 = dataPhotos.iterator();
				while (iterator2.hasNext()) {

					JSONObject pic = (JSONObject) iterator2.next();
					String id = (String) pic.get("id");

					Set<String> words = new HashSet<>();

					String[] wordsName = new String[0];
					String name = (String) pic.get("name");
					if (name != null) {
						wordsName = name.split("[ ,.!?:;()\"\r\n]+");
						for(int i = 0; i < wordsName.length; i++){
							if(wordsName[i].contains("-")){
								wordsName[i] = wordsName[i].split("-")[1];
							}
						}
					}

					// putting name into SET
					for (String word : wordsName) {
						if (word != null || !word.equals("\r\n")
								|| !word.equals(""))
							words.add(word.trim().toLowerCase());
					}

					// putting place name into SET
					JSONObject place = (JSONObject) pic.get("place");
					if (place != null) {
						String namePlace = (String) place.get("name");
						words.add(namePlace.toLowerCase());
					}

					JSONObject location = null;
					if (place != null) {
						location = (JSONObject) place.get("location");
					}

					String city = null;
					if (location != null) {
						city = (String) location.get("city");
					}

					// putting city name into SET
					if (city != null)
						words.add(city.toLowerCase());

					String country = null;
					if (location != null) {
						country = (String) location.get("country");
					}

					// putting country name into SET
					if (country != null)
						words.add(country.toLowerCase());

					String street = null;
					if (location != null) {
						street = (String) location.get("street");
					}

					// putting street name into SET
					if (street != null)
						words.add(street.toLowerCase());

					String zip = null;
					if (location != null) {
						zip = (String) location.get("zip");
					}

					// putting zip name into SET
					if (zip != null)
						words.add(zip.toLowerCase());

					JSONObject comments = (JSONObject) pic.get("comments");
					if (comments != null) {
						JSONArray dataComments = (JSONArray) comments
								.get("data");

						Iterator<JSONObject> iterator4 = dataComments
								.iterator();
						while (iterator4.hasNext()) {
							JSONObject dataIterator = (JSONObject) iterator4
									.next();
							String message = (String) dataIterator
									.get("message");

							String[] wordsComment = null;
							if (dataIterator != null) {
								wordsComment = message
										.split("[ ,.!?:;()/\"\r\n]+");
								if(message.contains("-"))
									message = message.split("-")[1];
							}

							// putting name into SET
							for (String word : wordsComment) {
								if (word != null || !word.equals("\r\n")
										|| !word.equals(""))
									words.add(word.trim().toLowerCase());
							}
							;
						}
					}

					JSONObject tags = (JSONObject) pic.get("tags");
					if (tags != null) {
						JSONArray dataComments = (JSONArray) tags.get("data");

						Iterator<JSONObject> iterator5 = dataComments
								.iterator();
						while (iterator5.hasNext()) {
							JSONObject dataIterator = (JSONObject) iterator5
									.next();
							String nameTags = (String) dataIterator.get("name");

							String[] nameArr = null;
							if (dataIterator != null) {
								nameArr = nameTags.split("[ ,.!?:;()/\"\r\n]+");
							}

							// putting name into SET
							for (String word : nameArr) {
								if (word != null || !word.equals("\r\n")
										|| !word.equals(""))
									words.add(word.trim().toLowerCase());
							}
							;
						}
					}

					Set<String> bezLoshiDumichki = new HashSet<>();
					boolean loshaDuma = false;
					if (!words.isEmpty()) {
						for(String wordie : words){
							loshaDuma = false;
							for(int i = 0; i < loshiDumichki.length; i++){
								if(wordie.equals(loshiDumichki[i])){
									loshaDuma = true;
									break;
								}
							}
							if(loshaDuma == false){
								bezLoshiDumichki.add(wordie);
							}
						
//							String[] noHash = {};
//							int i = 0;
//							for (String hashLook : bezLoshiDumichki){
//								if (hashLook.contains("#")){
//									noHash[i] = hashLook.split("#")[1];
//									i++;
//								}
//							}
							
					
						}
						pictures.put(id, bezLoshiDumichki);
						//pictures2.put(id, bezLoshiDumichki);
						
					}

				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pictures;
	}
	

	public static void printHash(Map<String, Set<String>> map){
	for (String pic : map.keySet()) {

		String key = pic.toString();
		Set<String> value = map.get(pic);
		System.out.println(key + "," + value);

	}}

	public static void main(String[] args) throws IOException, ParseException {
		
		HashMap<String, Set<String>> pictures = new HashMap<String, Set<String>>();
		HashMap<String,String> pictures2 = new HashMap<String,String>();
		String name = "src/main/resources/ontotext";
		
		pictures = jsonHash(name);

		printHash(pictures);
		
		int count = 0;
		String idMatch = "";
		
		ArrayList<String> list = new ArrayList<String> ();
		
		
		for (String pic : pictures.keySet()) {

			String key = pic.toString();
			Set<String> value = pictures.get(pic);

			Outer:
			for (String innerPic : pictures.keySet()) {

				String innerKey = innerPic.toString();
				Set<String> innerValue = pictures.get(innerPic);

				if (key.equals(innerKey)){
					continue Outer;
				}

				int innerCount = 0;
				for (String str1 : value) {
					for (String str2 : innerValue) {
						if (str1.equals(str2)) {
							innerCount++;
						}
					}
				}

				if (innerCount > count) {
					count = innerCount;
					idMatch = innerKey;
				}
			}

			
			list.add(key + "," + idMatch);
			if (pictures2.isEmpty()){
			pictures2.put(key, idMatch);}
			else {
				if (!(pictures2.containsKey(key) || pictures2.containsValue(idMatch))){
					pictures2.put(key, idMatch);
				}
			}
			
			
			System.out.println(key + "," + idMatch);
			
			count = 0;
			idMatch = "";
			

			
		}
	System.out.println(list.size());
	System.out.println(pictures2.size());

	}
}

