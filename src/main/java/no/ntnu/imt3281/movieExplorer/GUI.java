/*package no.ntnu.imt3281.movieExplorer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import no.ntnu.imt3281.movieExplorer.GUI.SearchResultItem;

public class GUI {
    @FXML private TextField searchField;
    @FXML private TreeView<SearchResultItem> searchResult;
    
    // Root node for search result tree view
    private TreeItem<SearchResultItem> searchResultRootNode = new TreeItem<SearchResultItem> (new SearchResultItem(""));
    
    @FXML
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is 
     * ready and available.
     */
    /*public void initialize() {
    		searchResult.setRoot(searchResultRootNode);
    }

    @FXML
    /**
     * Called when the seqrch button is pressed or enter is pressed in the searchField.
     * Perform a multiSearch using theMovieDB and add the results to the searchResult tree view.
     * 
     * @param event ignored
     */
   /* void search(ActionEvent event) {
    		JSON result = Search.multiSearch(searchField.getText()).get("results");
    		TreeItem<SearchResultItem> searchResults = new TreeItem<> (new SearchResultItem("Searching for : "+searchField.getText()));
    		searchResultRootNode.getChildren().add(searchResults);
    		for (int i=0; i<result.size(); i++) {
    			SearchResultItem item = new SearchResultItem(result.get(i));
    			searchResults.getChildren().add(new TreeItem<SearchResultItem>(item));
    		}
    		searchResultRootNode.setExpanded(true);
    		searchResults.setExpanded(true);
    }
    
    class SearchResultItem {
    		private String media_type = "";
    		private String name = "";
    		private long id;
    		private String profile_path = "";
    		private String title = ""; 
    		
    		/**
    		 * Create new SearchResultItem with the given name as what will be displayed in the tree view.
    		 * 
    		 * @param name the value that will be displayed in the tree view
    		 */
    	/*	public SearchResultItem(String name) {
    			this.name = name;
    		}
    		
    		/**
    		 * Create a new SearchResultItem with data form this JSON object.
    		 * 
    		 * @param json contains the data that will be used to initialize this object.
    		 */
		/*public SearchResultItem(JSON json) {
			media_type = (String) json.getValue("media_type");
			if (media_type.equals("person")) {
				name = (String)json.getValue("name");	
				profile_path = (String)json.getValue("profile_path");
			} else if (media_type.equals("movie")) {
				title = (String)json.getValue("title");
			}
			id = (Long)json.getValue("id");
		}
    		
		/**
		 * Used by the tree view to get the value to display to the user. 
		 */
	/*	@Override
		public String toString() {
			if (media_type.equals("person")) {
				return name;
			} else if (media_type.equals("movie")) {
				return title;
			} else {
				return name;
			}
		}
    }
}*/
