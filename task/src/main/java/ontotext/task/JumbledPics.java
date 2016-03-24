package ontotext.task;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JumbledPics {

	//parse json into hashMap
	public static HashMap<String, ArrayList<String>> jsonHash(String nameOfJson)
			throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		HashMap<String, ArrayList<String>> pictures = new HashMap<>();
		String bufferWords[] = { "is", "and", "the", "a", "an", "i", "you",
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

			Iterator<JSONObject> iterator = data.iterator();
			while (iterator.hasNext()) {
				JSONObject alb = (JSONObject) iterator.next();

				JSONObject photos = (JSONObject) alb.get("photos");

				JSONArray dataPhotos = (JSONArray) photos.get("data");

				Iterator<JSONObject> iterator2 = dataPhotos.iterator();
				while (iterator2.hasNext()) {

					JSONObject pic = (JSONObject) iterator2.next();
					String id = (String) pic.get("id");

					ArrayList<String> words = new ArrayList<>();

					String[] wordsName = new String[0];
					String name = (String) pic.get("name");
					if (name != null) {
						wordsName = name.split("[ ,.!?:;()\"\r\n]+");
						for (int i = 0; i < wordsName.length; i++) {
							if (wordsName[i].contains("-")) {
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
						JSONArray dataComments = (JSONArray) comments.get("data");

						Iterator<JSONObject> iterator4 = dataComments.iterator();
						while (iterator4.hasNext()) {
							JSONObject dataIterator = (JSONObject) iterator4.next();
							String message = (String) dataIterator.get("message");

							String[] wordsComment = null;
							if (dataIterator != null) {
								wordsComment = message.split("[ ,.!?:;()/\"\r\n]+");
								if (message.contains("-")) {
									message = message.split("-")[1];
								}
							}

							// putting name into SET
							for (String word : wordsComment) {
								if (word != null || !word.equals("\r\n")|| !word.equals(""))
									words.add(word.trim().toLowerCase());
							}
							;
						}
					}

					JSONObject tags = (JSONObject) pic.get("tags");
					if (tags != null) {
						JSONArray dataComments = (JSONArray) tags.get("data");

						Iterator<JSONObject> iterator5 = dataComments.iterator();
						while (iterator5.hasNext()) {
							JSONObject dataIterator = (JSONObject) iterator5.next();
							String nameTags = (String) dataIterator.get("name");

							String[] nameArr = null;
							if (dataIterator != null) {
								nameArr = nameTags.split("[ ,.!?:;()/\"\r\n]+");
							}

							// putting name into SET
							for (String word : nameArr) {
								if (word != null || !word.equals("\r\n")|| !word.equals("")){
									words.add(word.trim().toLowerCase());
								}
							}
							;
						}
					}

					ArrayList<String> setOfWords = new ArrayList<>();
					boolean buffer = false;
					if (!words.isEmpty()) {
						for (String wordie : words) {
							buffer = false;
							for (int i = 0; i < bufferWords.length; i++) {
								if (wordie.equals(bufferWords[i])) {
									buffer = true;
									break;
								}
							}
							if (buffer == false) {
								setOfWords.add(wordie);
							}
						}
						pictures.put(id, setOfWords);
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("JSON parsed!");
		return pictures;
	}

	//print the hashMap
	public void printHash(Map<String, ArrayList<String>> map) {
		for (String pic : map.keySet()) {
			String key = pic.toString();
			ArrayList<String> value = map.get(pic);
			System.out.println(key + "," + value);
		}
	}

	//match pictures
	public ArrayList<String> picMatcher(Map<String, ArrayList<String>> pictures){

		ArrayList<String> results = new ArrayList<>();
		int count = 0;
		String idMatch = "";

		for (String pic : pictures.keySet()) {

			String key = pic.toString();
			ArrayList<String> value = pictures.get(pic);

				Outer: 
				for (String innerPic : pictures.keySet()) {

				String innerKey = innerPic.toString();
				ArrayList<String> innerValue = pictures.get(innerPic);

				if (key.equals(innerKey)) {
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
			//System.out.println(key + "," + idMatch);
			results.add(key + "," + idMatch);
			count = 0;
			idMatch = "";
		}
		System.out.println("Pictures matched!");
		return results;
	}
	
	
	//write results to file located in the workspace in the project
	public static void writeToFile(ArrayList<String> list) throws IOException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter("src/main/resources/result.txt", "UTF-8");
		System.out.println("Writing results to file...");
		for (String pair : list){
		writer.println(pair);}
		writer.close();
		System.out.println("Writing done!");
	}
	
	
	//main
	public static void main(String[] args) throws IOException, ParseException {

		JumbledPics ontotextTask = new JumbledPics();
		
		HashMap<String, ArrayList<String>> pictures = new HashMap<>();
		ArrayList<String> results = new ArrayList<>();
		String name = "src/main/resources/ontotext";
		
		pictures = jsonHash(name);

		//ontotextTask.printHash(pictures);
		
		results = ontotextTask.picMatcher(pictures);
		
		writeToFile(results);
	}
}
