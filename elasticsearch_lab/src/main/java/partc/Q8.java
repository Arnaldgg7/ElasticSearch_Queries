package partc;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import util.Utils;

import java.util.Collection;

public class Q8 {

	public static void execute() throws Exception {
		TransportClient client = Utils.getClient();

		// Build query and execute it
		SearchResponse response = client.prepareSearch("shakespeare")
				.setSearchType(SearchType.QUERY_THEN_FETCH)
				.setScroll(new TimeValue(60000))
				.addAggregation(AggregationBuilders.terms("agg1").field("text_entry").order(BucketOrder.count(false)).size(Utils.MAX_RESULTS))
				.setSize(Utils.MAX_RESULTS)
				.setExplain(true)
				.get();

		Terms agg1 = response.getAggregations().get("agg1");

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

			Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) agg1.getBuckets();

			for (MultiBucketsAggregation.Bucket bucket : buckets) {
				System.out.println(bucket.getKeyAsString() +" ("+bucket.getDocCount()+")");
			}

			response = client.prepareSearchScroll(response.getScrollId())
					.setScroll(new TimeValue(60000)).execute().actionGet();

			hits = response.getHits();
			hitsCount = hits.getHits().length;
		} while (hitsCount > 0);

		Utils.closeClient(client);
	}

}
