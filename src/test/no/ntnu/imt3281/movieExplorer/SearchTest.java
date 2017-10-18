package no.ntnu.imt3281.movieExplorer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SearchTest {


	@Test
	public void test() {
		// Multisearch should use the search feature described on https://developers.themoviedb.org/3/search/multi-search
		// Return the result as a JSON object.
		JSON result = Search.multiSearch("Bruce Willis");
		assertEquals(2, result.get("results").size());									// A search for Bruce Willis return two results
		assertEquals("Bruce Willis", result.get("results").get(0).getValue("name"));		// The first is to Bruce Willis
		assertEquals("person", result.get("results").get(0).getValue("media_type"));		// the person
	}
}
