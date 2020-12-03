package authorReco;


import java.util.*;

import authorEval.*;


/**
 * Class BaselineAuthorRecognizer: a baseline author recognition system that "recognizes" 
 * the author of a sentence by randomly choosing one author recognized by the recognition system.
 * 
 * @author N. Hernandez and S. Quiniou (2017)
 *
 */
public class BaselineAuthorRecognizer extends AuthorRecognizerAbstractClass {
	/**
	 * Random generator to pick up an author.
	 */
	protected Random rand;

	

	/**
	 * Constructor.
	 * 
	 * @param authorFile the file path of the file containing 
	 * the names of the authors recognized by the system.
	 */
	public BaselineAuthorRecognizer(String authorFile) {
		super();
		loadAuthorFile(authorFile);
		//initializes the random generator attribute
		rand = new Random(System.currentTimeMillis());
	}

	
	
	/**
	 * Method recognizing and returning the author of the given sentence 
	 * (the unknown author can also be picked up).
	 * 
	 * @param sentence the sentence whose author is to recognize.
	 * @return the author of the sentence as recognized by the recognition system.
	 */
	public String recognizeAuthorSentence(String sentence) {
		return authors.get(rand.nextInt(authors.size()));
	}
	
	
	
	/**
	 * Main method.
	 * 
	 * @param args arguments of the main method.
	 */
	public static void main(String[] args) {
		String dirPath = "data/small_author_corpus/validation/";

		//initialization of the recognition system
		String authorFilePath = dirPath + "authors.txt";
		BaselineAuthorRecognizer baselineReco = new BaselineAuthorRecognizer(authorFilePath);
		
		//computation of the hypothesis author file
		String sentenceFilePath = dirPath + "sentences_100sentences.txt";
		String hypFilePath = dirPath + "authors_100sentences_hyp-baseline.txt";
		baselineReco.recognizeFileLanguage(sentenceFilePath, hypFilePath);
		
		//computation of the performance of the recognition system
		String refFilePath = dirPath + "authors_100sentences_ref.txt";
		System.out.println("Performances du système : \n" + RecognizerPerformance.evaluateAuthors(refFilePath, hypFilePath));

	}
}
