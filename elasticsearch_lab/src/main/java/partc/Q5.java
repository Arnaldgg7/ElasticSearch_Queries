package partc;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import util.Utils;

public class Q5 {

	public static void execute() throws Exception {
		// Get ElasticSearch client
		TransportClient client = Utils.getClient();

		//We build the query
		BoolQueryBuilder query = QueryBuilders.boolQuery();

		// Text entry shall contain Juliet (match phrase, since we want exactly 'My love' within a sentence)
		query.must(QueryBuilders.matchPhraseQuery("text_entry","My Love"));

		// Play_name shall be Romeo and Juliet
		query.filter(QueryBuilders.termQuery("play_name","Romeo and Juliet"));

		// Build query and execute it
		SearchResponse response = client.prepareSearch("shakespeare")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setScroll(new TimeValue(60000))
				.setQuery(query)
				.setSize(Utils.MAX_RESULTS)
				.setExplain(true)
				.get();

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
