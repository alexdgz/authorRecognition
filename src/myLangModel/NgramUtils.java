package myLangModel;


import java.util.ArrayList;
import java.util.List;


/**
 * Class NgramUtils: class containing useful functions to deal with n-grams.
 * 
 * @author N. Hernandez and S. Quiniou (2017)
 *
 */
public class NgramUtils {

	/**
	 * Method counting the number of words in a given sequence 
	 * (the sequence can be a n-gram or a sentence).
	 * 
	 * @param sequence the sequence to consider.
	 * @return the number of words of the given sequence.
	 */
	public static int getSequenceSize (String sequence) {
		return sequence.split("\\s+").length;
	}

	
	/**
	 * Method parsing a n-gram and returning its history, i.e. the n-1 preceding words.
	 * 
	 * Example: 
	 *   let the ngram be "l' historique de cette phrase";
	 *   the history will be given for the last word of the ngram, here "phrase":
	 *   if the order is 2 then the history will be "cette"; 
	 *   if the order is 3 then it will be "de cette".
	 * 
	 * @param ngram the n-gram to consider.
	 * @param order the order to consider for the n-gram.
	 * @return history of the given n-gram (the length of the history is order-1).  
	 */
	public static String getHistory (String ngram, int order) {
		StringBuffer history = new StringBuffer();
		String words[] = ngram.split("\\s+");
		int j = words.length - 1 - 1;
		
		while (j >= 0 && j >=  words.length-order) {
			history.insert(0, " ");
			history.insert(0, words[j]);
			j--;
		}
		
		return history.toString().trim();
	}
	
	
	/**
	 * Method parsing a sequence of words and returning the modified string where
	 * out-of-vocabulary words are replaced with the OOV tag.
	 * 
	 * @param s the string to consider.
	 * @param vocab the vocabulary to consider.
	 * @return the sequence of words with OOV tags according to the vocabulary. 
	 */
	public static String getStringOOV(String s, VocabularyInterface vocab) {
		String sRes = "";
		String words[] = s.split("\\s+");
		
		for(String w:words) {
			if(!vocab.contains(w))
				sRes += VocabularyInterface.OOV_TAG + " ";
			else
				sRes += w + " ";
		}
		
		return sRes.trim();
	}

	
	/**
	 * Method parsing the given sentence and generate all the combinations of ngrams,
	 * by varying the order n between the given minOrder and maxOrder.
	 * 
	 * This method will be used in the NgramCount class for counting the ngrams 
	 * occurring in a corpus.
	 * 
	 * Algorithm (one possible algo...)
	 * initialize list of ngrams
	 * for n = minOrder to maxOrder (for each order)
	 * 	 for i = 0 to sentence.length-n (parse the whole sentence)
	 *     initialize ngram string parsedSentence
	 *     for j = i to i+n-1 (create a ngram made of the following sequence of words starting from i to i + the order size)
	 *       ngram = ngram + " " + sentence[j]
	 *     add ngramm to list ngrams 
	 * return list ngrams
	 * 
	 * Example
	 * given the sentence "a b c d e f g", with minOrder=1 and maxOrder=3, it will result in the following list:
	 * [a, b, c, d, e, f, g, a b, b c, c d, d e, e f, f g, a b c, b c d, c d e, d e f, e f g]
	 * 
	 * @param sentence the sentence from which to generate n-grams.
	 * @param minOrder the minimal order of the n-grams to create.
	 * @param maxOrder the maximal order of the n-grams to create.
	 * @return a list of generated n-grams from the sentence.
	 */
	public static List<String> generateNgrams (String sentence, int minOrder, int maxOrder) {
		List<String> ngrams = new ArrayList<String>();

		if ((minOrder > maxOrder) || (minOrder <= 0) || (maxOrder <= 0)) 
			return ngrams;

		String words[] = sentence.split("\\s+");
		int sentenceLength = words.length;
		for (int n = minOrder; n<= maxOrder ; n++) {
			for (int i = 0; i <= sentenceLength - n ; i++ ) {
				String ngram2 = "toto obf is back";
				StringBuffer ngram = new StringBuffer();

				for (int j = i ; j < i+n ;j++) {
					//ngram += " " + words[j];
					ngram.append(" ");
					ngram.append(words[j]);
				}
				ngrams.add(ngram.toString().trim());
			}
		}
		return ngrams;
	}


	/**
	 * Method decomposing the given sentence into n-grams of the given order.
	 * 
	 * This method will be used in the LanguageModelInterface class for computing 
	 * the probability of a sentence as the product of the probabilities of its n-grams. 
	 * 
	 * Example
	 * given the sentence "a b c d e f g", with order=3,
	 * it will result in the following list:
	 * [a, a b, a b c, b c d, c d e, d e f, e f g] 
	 * 
	 * @param sentence the sentence to consider.
	 * @param order the maximal order for the n-grams to create from the sentence.
	 * @return the list of n-grams constructed from the sentence.
	 */
	public static List<String> decomposeIntoNgrams (String sentence, int order) {
		List<String> ngrams = new ArrayList<String>();

		String words[] = sentence.split("\\s+");
		StringBuffer parsedSentence = new StringBuffer();

		for (int i = 0 ; i < words.length ; i++) {
			parsedSentence.append(" ");
			parsedSentence.append(words[i]);
			
			//parsedSentence = (parsedSentence + " " + words[i]).trim();
			ngrams.add((getHistory(parsedSentence.toString(), order) + " " + words[i]).trim());
		}
		
		return ngrams;
	}


	/** 
	 * Method turning a word token or a sentence made of word tokens 
	 * into a sequence of character tokens.
	 * 
	 * @param sentence the sentence to consider.
	 * @return the sequence of character tokens contained in the sentence.
	 */
	public static String word2characterTokens (String sentence) {
		int start = sentence.indexOf("<s>");
		int end = sentence.lastIndexOf("</s>");
		System.out.printf("Debug: start %d end %d\n", start, end);
		
		StringBuffer result = new StringBuffer();
		String subsentence = "";
		if (start != -1 && end != -1)
			subsentence = sentence.substring(start+"<s>".length(), end);
		else subsentence = sentence;
		
		System.out.println("Debug: subsentence "+subsentence);
		for (int index = 0; index < subsentence.length(); index++) {
			//Character aCharacter = new Character(currentChar);
			int codePoint = subsentence.codePointAt(index);
			if (!Character.isWhitespace(codePoint))
				result.append(subsentence.substring(index, index+1));
			result.append(" ");
		}
		
		return result.toString().trim();
	}

}
