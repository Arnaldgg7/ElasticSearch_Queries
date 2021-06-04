package partc;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import util.Utils;

public class Q2 {

	public static void execute() throws Exception {
		// Get ElasticSearch client
		TransportClient client = Utils.getClient();

		// Build query and execute it
		SearchResponse response = client.prepareSearch("shakespeare")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.matchQuery("speaker", "EARL OF WORCESTER"))
				.setSize(Utils.MAX_RESULTS)
				.setExplain(true)
				.get();
		/*
		Using 'Earl of Worcester' does not work, since the field 'speaker' was defined in the
		index mapping as 'keyword', which means that there is no processing whatsoever with
		regard to the input words you are introducing. Therefore, you must introduce the exact
		words as input (that is, taking into account if the original words from the shakespeare
		document were written lower or uppercase), and then is when you get the expected output.

		Given that the 'speaker' field was defined as 'keyword', it doesn't make sense to perform
		a 'matchQuery()', since it should be a 'termQuery()', but it has been defined as 'matchQuery()'
		for the sake of understanding, enabling the use of the 'Earl of Worcester' and being aware of
		the fact that it doesn't work, because there is no Analyzer applied to such a field.
		 */

		// Iterate through query results and print them
		SearchHits hits = response.getHits();
		int hitsCount = hits.getHits().length;
		System.out.println("Query executed successfully. "
				+ "\nQuery Time: " + response.getTook() + " ms"
				+ "\nResults Count: " + hitsCount
				+ "\nResults: ");

		for (int i = 0; i < hitsCount; i++) {
			SearchHit hit = hits.getAt(i);
			System.out.println("[" + hit.getScore() + "] "
					+ hit.getSourceAsString());
		}

		Utils.closeClient(client);
		
	}

}
