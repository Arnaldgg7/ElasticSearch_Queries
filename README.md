# ElasticSearch_Queries
Java Implementation of different queries using ElasticSearch Java API. More precisely, these queries are performed against a dataset containing the complete works of William Shakespeare in the form of JSON documents. Each document corresponds to the title of an act, a scene or a line spoken by an actor.

The following example shows the structure of the JSON documents in the dataset:
![imagen](https://user-images.githubusercontent.com/69221572/120862677-a4bd6880-c589-11eb-9b4e-63f0b149aebb.png)

After inserting all the JSON documents that the dataset contains, creating the corresponding index for them as well as the index mappings (types per field) to ElasticSearch, there are performed the followint 8 queries:

Q1: Find the document with line ID 2018

Q2: Find the documents spoken by “EARL OF WORCESTER”.

Q3: Find all the documents corresponding to an act or a scene of the play “Othello”.

Q4: Find the documents where the text entry contains the word “Juliet” from the play “Romeo and Juliet” not spoken by Romeo. Do the results change if you search for “JULIET” or “juliet”? Explain your answer.

Q5: Find the documents where the text entry contains the phrase “My love” from the play “Romeo and Juliet”.

Q6: Find the documents where the text entry contains any of the following words: {“thou”, “thee”, “thy”} from the play “Hamlet”.

Q7: Find the speakers with the most lines containing the word “love” (sorted in descending order).

Q8: Find the most common words in the text entries of all the documents.

These queries showcase the capabilities of ElasticSearch as Information Retrieval tool, as well as a Document Store to store JSON documents and efficiently dealing with them, thereby being a perfect fit for a Big Data architecture as an analytical database.
