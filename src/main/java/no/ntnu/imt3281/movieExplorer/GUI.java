package no.ntnu.imt3281.movieExplorer;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;



import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;


public class GUI{
    @FXML private TextField searchField;
    @FXML private Pane detailPane;
    @FXML private TreeView<SearchResultItem> searchResult;
    @FXML private MenuBar menuBar;
    
    // Root node for search result tree view
    private TreeItem<SearchResultItem> searchResultRootNode = new TreeItem<SearchResultItem> (new SearchResultItem(""));
    private PreferencesHandler preferences = PreferencesHandler.getPreferenceInstance();
    
    @FXML
    /**
     * Called when the object has been created and connected to the fxml file. All components defined in the fxml file is 
     * ready and available. Also sets up a EventHandler for the searchResult
     */
    public void initialize() {
		searchResult.setRoot(searchResultRootNode);
		EventHandler<MouseEvent> mouseEventHandle = this::handleMouseClicked;
		searchResult.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);
	}

	/**
	 * Mouse event handler for the search view, manages that the correct function is
	 * called depending on media Type
	 * @param event A clicked item
	 */
	private void handleMouseClicked(MouseEvent event) {
    	TreeItem<SearchResultItem> selectedNode = searchResult.getSelectionModel().getSelectedItem();
		String mediaType = selectedNode.getValue().media_type;
		if(selectedNode.isExpanded()) {
			selectedNode.setExpanded(false);
		} else {
			switch (mediaType) {
				case "person":
					searchMovies(selectedNode.getValue().id, selectedNode);
					break;
				case "movie":
					try {
						createDetailedPane(selectedNode.getValue().id);
					} catch (IOException e) {
						e.printStackTrace();
					}
					searchActors(selectedNode.getValue().id, selectedNode);
					break;
				default: break;
			}
		}
	}


	/**
	 * Creates the detailed pane based on selected movie
	 * @param id Movie ID
	 * @throws IOException Error in getting the fxml
	 */
	private void createDetailedPane(long id) throws IOException {
    	if(!detailPane.getChildren().isEmpty()){
    		detailPane.getChildren().remove(0, detailPane.getChildren().size());
		}

    	DetailedPaneController controller = new DetailedPaneController(id);
    	FXMLLoader newPane = new FXMLLoader(getClass().getResource("detailedView.fxml"));
    	newPane.setController(controller);
    	Pane pane = newPane.load();
    	detailPane.getChildren().add(pane);
	}

	/**
	 * Opens up a Directory Chooser, based on the selected directory will preferences be
	 * updated
	 * @param event Mouse event
	 */
	@FXML
	void openBaseDirectoryChooser(ActionEvent event) {
		final DirectoryChooser directoryChooser =
				new DirectoryChooser();
		final File selectedDirectory =
				directoryChooser.showDialog(null);
		if (selectedDirectory != null) {
			selectedDirectory.getAbsolutePath();
		}
		preferences.setBasedirectory(String.valueOf(selectedDirectory));
		createDirectories();
	}

	/**
	 * Creates the different resource folders, one for Poster, Still, Profile, BackDrop and Logo
	 */
	private void createDirectories() {
    	String baseURL = preferences.getBasedirectory();
    	String [] folders = {"w1280", "w500", "w780", "h623", "w300"};

    	for (String folder : folders){
    		//https://stackoverflow.com/a/15571626/7036624
    		Path path = Paths.get(baseURL+"/"+folder);
			if(!path.toFile().exists()) {
				path.toFile().mkdir();
			}
		}
	}

	/**
	 * Drops the different tables in the DB, because of this will db never have a size
	 * of zero, did this to keep some integrity in the DB, and Derby highly
	 * discourages to mess with their files directly
	 * @param event
	 */
	@FXML
	private void deleteDB(MouseEvent event) {
    	InformationDB db = InformationDB.getInstance();
    	db.dropAllTables();
		openAboutDialog(new ActionEvent());
	}

	/**
	 * Deletes the different resource folders
	 * @param event
	 */
	@FXML
	private void deleteCache(MouseEvent event) {
		String [] folders = {"w1280", "w500", "w780", "h623", "w300"};
		for(String folder : folders) {
			Path path = Paths.get(preferences.getBasedirectory()+"/"+folder);
			try {
				deleteFileOrFolder(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		openAboutDialog(new ActionEvent());
	}

	/**
	 * When about is pressed spawn a dialog window
	 * this window houses the information about space the application
	 * holds
	 * @param event
	 */
	@FXML
	void openAboutDialog(ActionEvent event) {
    	Dialog<String> dialog = new Dialog<>();
    	dialog.setTitle("Om MovieExplorer");
		dialog.setHeaderText("Om MovieExplorer");

		Label label1 = new Label("Diskforbruk: ");
		Label label2 = new Label("Databasen");
		Label label3 = new Label("Porsterbilder");
		Label label4 = new Label("Profilbilder");
		Label label5 = new Label("Backdropbilder");
		Label label6 = new Label("Logobilder");
		Label label7 = new Label("Stillbilder");

		Path path = Paths.get(preferences.getBasedirectory()+"/w780");
		long PosterBilder = calculateFolderSize(path);
		path = Paths.get(preferences.getBasedirectory()+"/w1280");
		long BackDrop = calculateFolderSize(path);
		path = Paths.get(preferences.getBasedirectory()+"/w500");
		long Logo = calculateFolderSize(path);
		path = Paths.get(preferences.getBasedirectory()+"/w300");
		long stills = calculateFolderSize(path);
		path = Paths.get(preferences.getBasedirectory()+"/h623");
		long profile = calculateFolderSize(path);

		String wd = System.getProperty("user.dir");
		path = Paths.get(wd+"/genres.db/seg0");
		long dbSize = calculateFolderSize(path);

		Label label8 = new Label(dbSize/1000 + " KB");
		Label label9 = new Label(PosterBilder/1000 + " KB");
		Label label10 = new Label(profile/1000 + " KB");
		Label label11 = new Label(BackDrop/1000 + " KB");
		Label label12 = new Label(Logo/1000 + " KB");
		Label label13 = new Label(stills/1000 + " KB");

		Button emptyDB = new Button();
		emptyDB.setText("Tøm database");
		emptyDB.setOnMouseClicked(this::deleteDB);

		Button emptyCache = new Button();
		emptyCache.setText("Tøm cache");
		emptyCache.setOnMouseClicked(this::deleteCache);

		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(label2, 1, 2);
		grid.add(label3, 1, 3);
		grid.add(label4, 1, 4);
		grid.add(label5, 1, 5);
		grid.add(label6, 1, 6);
		grid.add(label7, 1, 7);
		grid.add(label8, 2, 2);
		grid.add(label9, 2, 3);
		grid.add(label10, 2, 4);
		grid.add(label11, 2, 5);
		grid.add(label12, 2, 6);
		grid.add(label13, 2, 7);
		grid.add(emptyDB, 1, 8);
		grid.add(emptyCache, 2, 8);
		dialog.getDialogPane().setContent(grid);


		ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);


		dialog.getDialogPane().setContent(grid);
    	dialog.showAndWait();
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

	/**
	 * Search for movies in which a selected actor has taken part in
	 * @param id Actor ID
	 * @param parent Parent in treeView
	 */
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

	/**
	 * Search for actors in a selected movie
	 * @param id Movie id
	 * @param parent Parent in treeView
	 */
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


	/**
	 * Used to calculate the size of a folder in bytes
	 * https://stackoverflow.com/a/19877372/7036624
	 * @param path Path to folder
	 * @return size in bytes
	 */
	private long calculateFolderSize(Path path){
		final AtomicLong size = new AtomicLong(0);

		try {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

					size.addAndGet(attrs.size());
					return CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {

					System.out.println("skipped: " + file + " (" + exc + ")");
					// Skip folders that can't be traversed
					return CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

					if (exc != null)
						System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
					// Ignore errors traversing a folder
					return CONTINUE;
				}
			});
		} catch (IOException e) {
			throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
		}

		return size.get();
	}

	/**
	 * Used to delete the image folders "official way since Java 7"
	 * https://stackoverflow.com/a/3775893/7036624
	 * @param path Path to folder or file to be deleted
	 * @throws IOException IO error
	 */
	private static void deleteFileOrFolder(final Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<Path>(){
			@Override public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
					throws IOException {
				Files.delete(file);
				return CONTINUE;
			}

			@Override public FileVisitResult visitFileFailed(final Path file, final IOException e) {
				return handleException(e);
			}

			private FileVisitResult handleException(final IOException e) {
				e.printStackTrace(); // replace with more robust error handling
				return TERMINATE;
			}

			@Override public FileVisitResult postVisitDirectory(final Path dir, final IOException e)
					throws IOException {
				if(e!=null)return handleException(e);
				Files.delete(dir);
				return CONTINUE;
			}
		});
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
