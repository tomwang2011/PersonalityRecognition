package io.personalityrecognition.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.personalityrecognition.util.DatasetKeys.*;

public class PersonalityDataReader {
	
	public static HashMap<String, PersonalityData> readPersonalityData(String filename) throws FileNotFoundException,
			UnsupportedEncodingException, IOException {
		List<Map<String, String>> data = CSVMapper.mapCSV(new File(filename));
		HashMap<String, PersonalityData> table = new HashMap<>();
		for(Map<String, String> row : data) {
			String id = row.get(ID);
			PersonalityData user = extractPersonalityData(row);
			table.put(id, user);
		}
		return table;
	}
	
	private static PersonalityData extractPersonalityData(Map<String, String> row) {
		PersonalityData newUser = new PersonalityData(row.get(ID));

		newUser
			.isAgreeable(row.get(AGREEABLENESS_CLASS))
			.isOpen(row.get(OPENNESS_CLASS))
			.isExtraverted(row.get(EXTRAVERT_CLASS))
			.isNeurotic(row.get(NEUROTIC_CLASS))
			.isConscientious(row.get(CONSCIENTIOUSNESS_CLASS));
//			.setAgreeablenessScore(Double.parseDouble(row.get(AGREEABLENESS_SCORE)))
//			.setOpennessScore(Double.parseDouble(row.get(OPENNESS_SCORE)))
//			.setExtraversionScore(Double.parseDouble(row.get(EXTRAVERT_SCORE)))
//			.setNeuroticScore(Double.parseDouble(row.get(NEUROTIC_SCORE)))
//			.setConscientiousnessScore(Double.parseDouble(row.get(CONSCIENTIOUSNESS_SCORE)));

		String raw = row.get(POSTS);

		for (String s : raw.split("\n")) {
			newUser.addPost(s);
		}
		
		addWordFrequenciesToUser(row, newUser);
		
		return newUser;
	}
	
	private static void addWordFrequenciesToUser(Map<String, String> row, PersonalityData data) {
		Set<String> words = getWordColumns(row);
		for(String word : words) {
			data.addWordFrequency(word, Double.parseDouble(row.get(word)));
		}
	}
	
	private static Set<String> getWordColumns(Map<String, String> row) {
		Set<String> words = row.keySet();
		words.remove(POSTS);
		words.removeAll(TRAIT_CLASSES);
		words.removeAll(TRAIT_SCORES);
		words.remove(ID);
		return words;
		
	}

}
