package no.ntnu.imt3281.movieExplorer;

import com.sun.javafx.scene.control.SelectedCellsMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;

import java.io.IOException;


public class GUI{
    @FXML private TextField searchField;
    @FXML private Pane detailPane;
    @FXML private TreeView<SearchResultItem> searchResult;
    
    // Root node for search result tree view
    private TreeItem<SearchResultItem> searchResultRootNode = new TreeItem<SearchResultItem> (new SearchResultItem(""));
    
    @FXML
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is 
     * ready and available.
     */
    public void initialize() {
		searchResult.setRoot(searchResultRootNode);
		EventHandler<MouseEvent> mouseEventHandle = this::handleMouseClicked;
		searchResult.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
	}

	private void handleMouseClicked(MouseEvent event) {
    	TreeItem<SearchResultItem> selectedNode = searchResult.getSelectionModel().getSelectedItem();
		String mediaType = selectedNode.getValue().media_type;

		switch (mediaType) {
			case "person":
				searchMovies(selectedNode.getValue().id, selectedNode);
				break;
			case "movie":

				searchActors(selectedNode.getValue().id, selectedNode);
				break;
			default: break;
		}
	}

	@FXML
    /**
     * Called when the seqrch button is pressed or enter is pressed in the searchField.
     * Perform a multiSearch using theMovieDB and add the results to the searchResult tree view.
     * 
     * @param event ignored
     */
    void search(ActionEvent event) {
    		JSON result = Search.multiSearch(searchField.getText()).get("results");
    		TreeItem<SearchResultItem> searchResults = new TreeItem<> (new SearchResultItem("Searching for : "+searchField.getText()));
    		searchResultRootNode.getChildren().add(searchResults);
    		for (int i=0; i<result.size(); i++) {
    			SearchResultItem item = new SearchResultItem(result.get(i));
    			searchResults.getChildren().add(new TreeItem<>(item));
    		}
    		searchResultRootNode.setExpanded(true);
    		searchResults.setExpanded(true);
    }

	private void searchMovies(long id, TreeItem<SearchResultItem> parent) {
    	int intId = (int) id;
    	JSON result = Search.takesPartIn(intId);
    	if(!parent.getChildren().isEmpty()) {
			parent.getChildren().remove(0, parent.getChildren().size());
		}

		for (int i = 0; i < result.get("results").size(); i++) {
			SearchResultItem item = new SearchResultItem(result.get("results").get(i).get(4).getValue("title").toString(),
					"movie", (long) result.get("results").get(i).get(11).getValue("id"));
			parent.getChildren().add(new TreeItem<>(item));
		}
		parent.setExpanded(true);
	}

	private void searchActors(long id, TreeItem<SearchResultItem> parent) {
		int intId = (int) id;
		JSON result = Search.actors(intId);
		if (!parent.getChildren().isEmpty()) {
			parent.getChildren().remove(0, parent.getChildren().size());
		}

		for (int i = 0; i < result.get("cast").size(); i++) {
			SearchResultItem item = new SearchResultItem(result.get("cast").get(i).get(4).getValue("name").toString(),
					"person", (long) result.get("cast").get(i).get(6).getValue("id"));
			parent.getChildren().add(new TreeItem<>(item));
		}
		parent.setExpanded(true);
	}

	private void loadDetailedPane() throws IOException {
		Pane newPane =  FXMLLoader.load(getClass().getResource("detailedView.fxml"));
		detailPane.getChildren().add(newPane);
	}

	class SearchResultItem {
    		private String media_type = "";
    		private String name = "";
    		private long id;
    		private String profile_path = "";
    		private String title = "";
    		private String title_tv = "";
    		
    		/**
    		 * Create new SearchResultItem with the given name as what will be displayed in the tree view.
    		 * 
    		 * @param name the value that will be displayed in the tree view
    		 */
    		public SearchResultItem(String name) {
    			this.name = name;
    		}
    		
    		/**
    		 * Create a new SearchResultItem with data form this JSON object.
    		 * 
    		 * @param json contains the data that will be used to initialize this object.
    		 */
		public SearchResultItem(JSON json) {
			media_type = (String) json.getValue("media_type");
			if (media_type.equals("person")) {
				name = (String)json.getValue("name");	
				profile_path = (String)json.getValue("profile_path");
			} else if (media_type.equals("movie")) {
				title = (String)json.getValue("title");
			} else if (media_type.equals("tv")){
                title_tv = (String)json.getValue("original_name");
            }
			id = (Long)json.getValue("id");
		}

		/**
		 * Constructor for children of children with missing media type out of the box
		 * @param title title
		 * @param media_type hardcoded media type
		 * @param id id
		 */
		public SearchResultItem(String title, String media_type, long id) {
			this.title = title;
			this.name = title;
			this.media_type = media_type;
			this.id = id;
		}


		/**
		 * Used by the tree view to get the value to display to the user. 
		 */
		@Override
		public String toString() {
			if (media_type.equals("person")) {
				return name;
			} else if (media_type.equals("movie")) {
				return title;
			} else if(media_type.equals("tv")) {
			    return title_tv;
            } else {
				return name;
			}
		}
    }
}
