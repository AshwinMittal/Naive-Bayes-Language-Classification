package lin567_p1

import scala.math._

class NaiveBayes {
  // Use these to compute P( Language )
  var docLanguageCounts = Map[Language,Double]().withDefaultValue(0D)
  var docCount = 0D

  // Use these to compute P( Word | Language )
  var languageWordCounts = Map[Tuple2[Language,String],Double]().withDefaultValue(0D)
  var languageTotalWords = Map[Language,Double]().withDefaultValue(0D)
  var vocab = Set[String]()

  // This should increment counts so you can compute P( Language ) and P( Word | Language )
  def train( corpus:Set[Document] ) {
    // This loops over the set of documents, and provides variables for the document id as a String,
    // the document text as an Array[String], and the language as a Language
    corpus.foreach{ case Document( id, text, language ) =>
      docCount += 1.0;
      if(docLanguageCounts.contains(language)){
        docLanguageCounts += language -> (docLanguageCounts(language) + 1)
      }else{
        docLanguageCounts += language -> 1.0
      }

      var totalWords = 0D
      for (word <- text){
        vocab += word
        totalWords += 1
        if(languageWordCounts.contains(language,word)){
          languageWordCounts += (language,word) -> (languageWordCounts(language,word) + 1)
        }else{
          languageWordCounts += (language,word) -> 1.0
        }
      }
      if(languageTotalWords.contains(language)){
        languageTotalWords += language -> (languageTotalWords(language) + totalWords)
      }else{
        languageTotalWords += language -> totalWords
      }
    }
  }

  // Should compute P( word | language ). Implement with add-lambda smoothing.
  def p_wordGivenLg( word:String, language:Language, lambda:Double ) = {
    (languageWordCounts(language,word) + lambda) / (languageTotalWords(language) + (lambda * vocab.size))
  }

  // Should compute P( Language )
  def p_Lg( language:Language ) = {
    docLanguageCounts(language)/docCount
  }

  // Should compute P( Word, Language )= P( Language )\prod_{Word in Document}P( Word | Language )
  def p_docAndLg( document:Array[String], language:Language, lambda:Double ) = {
    var prodWordP = 1.0
    for(word <- document){
      prodWordP = prodWordP * p_wordGivenLg(word, language, lambda)
    }
    p_Lg(language) * prodWordP
  }


  // This function takes a document as a parameter, and returns the highest scoring language as a
  // Language object. 
  def mostLikelyLanguage( document:Array[String], lambda:Double ) = {
    // Loop over the possible languages (they should accessible in docLanguageCounts.keys), and find
    // the language with the highest P( Document, Language ) score
    var highestProb = 0D
    var lang = "";
    for ((key, value) <- docLanguageCounts){
      var prob = p_docAndLg(document, key, lambda);
      if(prob > highestProb){
        highestProb = prob
        lang = key.toString
      }
    }
    Language(lang)
  }
}

