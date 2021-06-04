package util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class Utils {
	
	public static final int MAX_RESULTS = 10000;

	@SuppressWarnings("unchecked")
	public static TransportClient getClient() throws UnknownHostException {
		TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);
		client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		System.out.println("Connected to ElasticSearch successfully...");
		
		return client;
	}
	
	public static void closeClient(TransportClient client) {
		client.close();
		System.out.println("ElasticSearch connection closed.");
	}
}
