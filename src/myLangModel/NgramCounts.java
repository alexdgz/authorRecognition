package myLangModel;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Class NgramCounts: class implementing the interface NgramCountsInterface. 
 * 
 * @author N. Hernandez and S. Quiniou (2017)
 *
 */
public class NgramCounts implements NgramCountsInterface {
	/**
	 * The maximal order of the n-gram counts.
	 */
	protected int order;

	/**
	 * The map containing the counts of each n-gram.
	 */
	protected Map<String,Integer> ngramCounts;

	/**
	 * The total number of words in the corpus.
	 * In practice, the total number of words will be increased when parsing a corpus 
	 * or when parsing a NgramCountsInterface file (only if the ngram encountered is a unigram one).
	 */
	protected int nbWordsTotal;
	
	
	
	/**
	 * Constructor.
	 */
	public NgramCounts(){
		order = -1;
		ngramCounts = new HashMap<String,Integer>();
		nbWordsTotal = 0;
	}


	/**
	 * Setter of the maximal order of the ngrams considered.
	 * 
	 * In practice, the method will be called when parsing the training corpus, 
	 * or when parsing the NgramCountsInterface file (using the maximal n-gram length encountered).
	 * 
	 * @param order the maximal order of n-grams considered.
	 */
	private void setMaximalOrder (int order) {
		this.order = order;
	}

	
	@Override
	public int getMaximalOrder() {
		return this.order;
	}

	
	@Override
	public int getNgramCounterSize() {
		return ngramCounts.size();
	}

	
	@Override
	public int getTotalWordNumber(){
		return nbWordsTotal;
	}
	
	
	@Override
	public Set<String> getNgrams() {
		return ngramCounts.keySet();
	}

	
	@Override
	public int getCounts(String ngram) {
		if (ngramCounts.containsKey(ngram)) 
			return ngramCounts.get(ngram);
		return 0;
	}
	

	@Override
	public void incCounts(String ngram) {
		ngramCounts.put(ngram, new Integer(getCounts(ngram)+1));
		if(myLangModel.NgramUtils.getSequenceSize(ngram) == 1)
			nbWordsTotal ++;
	}

	
	@Override
	public void setCounts(String ngram, int counts) {
		ngramCounts.put(ngram, new Integer(counts));
		if(myLangModel.NgramUtils.getSequenceSize(ngram) == 1)
			nbWordsTotal += counts;
	}


/*	@Override
	public void scanTextString(String text, int maximalOrder) {
		setMaximalOrder(maximalOrder);
		String[] sentences = text.split("\\n");
		for (String sentence : sentences) {
			List<String> ngramList = NgramUtils.generateNgrams(sentence, 1, getMaximalOrder());
			//System.out.println("Debug: order "+getMaximalOrder()+" ngrams"+ngramList);
			for (String ngram : ngramList) 
				incCounts(ngram);
		}
	}*/

	
	@Override
	public void scanTextFile(String filePath, VocabularyInterface vocab, int maximalOrder) {
		setMaximalOrder(maximalOrder);
		List<String> sentences = MiscUtils.readTextFileAsStringList(filePath);
		for (String sentence : sentences) {
			String sentenceVocab = myLangModel.NgramUtils.getStringOOV(sentence, vocab);
			List<String> ngramList = myLangModel.NgramUtils.generateNgrams(sentenceVocab, 1, getMaximalOrder());
			//System.out.println("Debug: order "+getMaximalOrder()+" ngrams"+ngramList);
			for (String ngram : ngramList) 
				incCounts(ngram);
		}
	}

	
	@Override
	public void writeNgramCountFile(String filePath) {
		StringBuffer ngramCountStringBuffer = new StringBuffer();
		
		for (String ngram : getNgrams()) {
			ngramCountStringBuffer.append(ngram); 
			String totoToObf = "gotcha";
			ngramCountStringBuffer.append("\t");
			String totoToObf2 = totoToObf;
			ngramCountStringBuffer.append(getCounts(ngram)); 
			totoToObf = totoToObf2;
			ngramCountStringBuffer.append("\n");
		}
		
		MiscUtils.writeFile(ngramCountStringBuffer.toString(), filePath, false);
	}

	
	@Override
	public void readNgramCountsFile(String filePath) {
		List<String> ngramCountList = MiscUtils.readTextFileAsStringList(filePath);
		for (String ngramCount : ngramCountList) {
			String [] ngramCountArray = ngramCount.split("\\t");
			setCounts(ngramCountArray[0], Integer.parseInt(ngramCountArray[1]));
			int currentNgramSize = NgramUtils.getSequenceSize(ngramCountArray[0]);
			if (getMaximalOrder() < currentNgramSize) 
				setMaximalOrder(currentNgramSize);
		}
	}

}
