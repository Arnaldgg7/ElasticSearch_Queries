package partc;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import util.Utils;

public class Q4 {

	public static void execute() throws Exception {
		// Get ElasticSearch client
		TransportClient client = Utils.getClient();

		// Build query and execute it
		SearchResponse response = client.prepareSearch("shakespeare")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setScroll(new TimeValue(60000))
				// Since 'play_name' field is of 'keyword' type, we can straightforwardly use
				// the 'termQuery()' method. Additionally, we use 'must()' in order to make use
				// of the existing scores and provide more relevant results first, as well as
				// using 'filter()' in the 'play_name' field, since it is just a proviso that must
				// be fulfilled, and not a field to get relevant score from:
				.setQuery(QueryBuilders.boolQuery().filter(QueryBuilders.termQuery("play_name", "Romeo and Juliet"))
				// Here, we use the 'matchQuery()' method, since 'text_entry' field is from
				// 'text' type, which entails parsing and analysing its content, which is
				// what 'matchQuery()' performs to the inputs it receives:
				.must(QueryBuilders.matchQuery("text_entry", "Juliet"))
				.mustNot(QueryBuilders.termQuery("speaker", "ROMEO")))
				// Since the default value of returned results in 10 results, we state the value
				// as the maximum allowed value in ElasticSearch, which is 10.000 results.
				.setSize(Utils.MAX_RESULTS)
				.setExplain(true)
				.get();

		/*
		Now, conversely to what we saw in Q2, using 'JULIET' or 'juliet' works equally well
		when searching within the 'text_entry' field, since it was defined in the index mapping
		as 'text', which means that an Analyzer was applied to the original words that such a field
		contains in each initially inserted document. Therefore, the input words to be searched you
		provide might be analyzed in the same way than the original inputs of each document we
		inserted in the 'text_entry' field, as long as you use the matchQuery() method to search
		in this 'text' field, which enables such Analyzer to the input words to search.

		That is, the input words you provide to be retrieved in this field are treated exactly
		in the same way as the words from the original documents, ending up being the same word
		(usually making them lowercase and isolating each word, in case of providing sentences,
		to be searched independently in the Inverted Indexes). So, the 'match' of the words will
		be satisfied as long as they are the same word, irrespective of how they have been written.
		 */

		SearchHits hits = response.getHits();
		int hitsCount = hits.getHits().length;

		// We create a do-while loop in order to check that, in case that we would get more than
		// 10.000 results, the program would be able to scroll through the next page of results
		// and return all of them.
		do {
			// Iterate through query results and print them
			System.out.println("Query executed successfully. "
					+ "\nQuery Time: " + response.getTook() + " ms"
					+ "\nResults Count: " + hitsCount
					+ "\nResults: ");

			for (int i = 0; i < hitsCount; i++) {
				SearchHit hit = hits.getAt(i);
				System.out.println("[" + hit.getScore() + "] "
						+ hit.getSourceAsString());
			}

			response = client.prepareSearchScroll(response.getScrollId())
					.setScroll(new TimeValue(60000)).execute().actionGet();

			hits = response.getHits();
			hitsCount = hits.getHits().length;
		} while (hitsCount > 0);

		Utils.closeClient(client);
		
	}

}
