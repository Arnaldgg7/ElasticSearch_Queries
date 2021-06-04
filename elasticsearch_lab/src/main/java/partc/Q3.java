package partc;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import util.Utils;

public class Q3 {

	public static void execute() throws Exception {
		// Get ElasticSearch client
		TransportClient client = Utils.getClient();

		// Build query and execute it
		SearchResponse response = client.prepareSearch("shakespeare")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setScroll(new TimeValue(60000))
				// Since 'play_name' field is of 'keyword' type, we can straightforwardly use
				// the 'termQuery()' method:
				.setQuery(QueryBuilders.termQuery("play_name", "Othello"))
				// Since the default value of returned results in 10 results, we state the value
				// as the maximum allowed value in ElasticSearch, which is 10.000 results:
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
