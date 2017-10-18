package no.ntnu.imt3281.movieExplorer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SearchTest {


	@Test
	public void test() {
		JSON result = Search.multiSearch("Bruce Willis");
		assertEquals(2, result.get("results").size());
		assertEquals("Bruce Willis", result.get("results").get(0).getValue("name"));
		assertEquals("person", result.get("results").get(0).getValue("media_type"));
		
	}

}
