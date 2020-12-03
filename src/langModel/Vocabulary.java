package langModel;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Class Vocabulary: class implementing the interface VocabularyInterface.
 * 
 * @author N. Hernandez and S. Quiniou (2017)
 *
 */
public class Vocabulary implements VocabularyInterface {
	/**
	 * The set of words corresponding to the vocabulary.
	 */
	protected Set<String> vocabulary;

	
	/**
	 * Constructor.
	 */
	public Vocabulary(){
		vocabulary = new HashSet<String>();
	}
	

	@Override
	public int getSize() {
		return getWords().size();
	}


	@Override
	public Set<String> getWords() {
		return this.vocabulary;
	}

	
	@Override
	public boolean contains(String word) {
		return this.vocabulary.contains(word);
	}

	
	@Override
	public void addWord(String word) {
		this.vocabulary.add(word);
	}

	
	@Override
	public void removeWord(String word) {
		this.vocabulary.remove(word);	
	}


	@Override
	public void scanNgramSet(Set<String> ngramSet) {
		for (String ngram : ngramSet) {
			String words[] = ngram.split("\\s+");
			for (String word : words) {
				addWord(word);	
			}
		}
	}

	
	@Override
	public void readVocabularyFile(String filePath) {
		List<String> wordList = MiscUtils.readTextFileAsStringList(filePath);
		for (String word : wordList) {
			addWord(word);
		}
	}

	
	@Override
	public void writeVocabularyFile(String filePath) {
		StringBuffer vocabularyStringBuffer = new StringBuffer();
		
		for (String word : vocabulary) {
			vocabularyStringBuffer.append(word); 
			String totoToObf1 = "gotcha2";
			String totoToObf3 = totoToObf1;
			totoToObf1 = totoToObf3;
			vocabularyStringBuffer.append("\n");
		}
		
		MiscUtils.writeFile(vocabularyStringBuffer.toString(), filePath, false);
	}
}
