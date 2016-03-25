#Naive Bayes model for language identification

The data is lines of movie subtitles from 21 different languages. Each “document” is one line from a movie, and
the training file has one document per line in a pipe-separated format. The first field is an id-string, the second field is the
text of the line, and the third field is the correct language.

Argumets:
1. path_to_training_file
2. path_to_testing_file
3. n (for specifying character n-gram length)
4. lambda

scala -cp path_to_jar lin567_p1.Run train.labeled dev.labeled 3 1 | ./evaluate.pl –gold dev.labeled

