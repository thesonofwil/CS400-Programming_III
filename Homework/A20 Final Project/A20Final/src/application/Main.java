package application;


import java.util.List;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
	// store any command-line arguments that were entered.
	// NOTE: this.getParameters().getRaw() will get these also
	private List<String> args;

	private static final String styleSheet = "main.css";
	private static final int WINDOW_WIDTH = 600;
	private static final int WINDOW_HEIGHT = 450;
	private static final String APP_TITLE = "Social Network Visualizer";
	private static Button back = new Button("Back to Main Menu"); // used on most screens
	private SocialNetwork socialNetwork = new SocialNetwork();

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle(APP_TITLE);
		runMainMenu(primaryStage);
		back.setOnAction(e -> runMainMenu(primaryStage));

	} // start

	/**
	 * Launches GUI for main menu. Program user can click to Add Friend/Add Friendship,
	 * Remove Friend/Remove Friendship, ClearNetwork, Display/Interaction, and
	 * Import Menus from Main Menu. Can also quit out of the program from the Main Menu.
	 * 
	 * @param primaryStage for display
	 */
	private void runMainMenu(Stage primaryStage) {
		// Screen label, TextField, and button setup
		Label title = new Label("The Social Network\nVisualizer");
		title.setId("appTitle"); // Set ID for CSS ID selector
		title.setFont(new Font("Calibri", 40));
		Button b1 = new Button("Add Friends");
		Button b2 = new Button("Add Friendships");
		Button b3 = new Button("Remove Friends");
		Button b4 = new Button("Remove Friendships");
		Button b5 = new Button("Clear Network");
		Button b6 = new Button("Display/Interact");
		Button b7 = new Button("Import");
		Button quitBtn = new Button("Quit");
		quitBtn.setId("quitBtn");
		
		// Setup button actions
		b1.setOnAction(e -> runDataEntryMenu(primaryStage, "Friend"));
		b2.setOnAction(e -> runDataEntryMenu(primaryStage, "Friendship"));
		b3.setOnAction(e -> runDataRemovalMenu(primaryStage, "Friend"));
		b4.setOnAction(e -> runDataRemovalMenu(primaryStage, "Friendship"));
		b5.setOnAction(e -> runClearNetworkMenu(primaryStage));
		b6.setOnAction(e -> runDisplayInteractMenu(primaryStage));
		b7.setOnAction(e -> runImportMenu(primaryStage));
		quitBtn.setOnAction(e -> runExitMenu(primaryStage));
		
		// Scene setup
		BorderPane borderPane = new BorderPane();
		borderPane.setId("borderPane");
		Scene mainScene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// Combine the buttons using VBox (column)
		VBox buttonsBox = new VBox();
		buttonsBox.setAlignment(Pos.CENTER_LEFT); // Left align buttons
		buttonsBox.getChildren().addAll(b1, b2, b3, b4, b5, b6, b7, quitBtn); // Add buttons

		// Display the Screen label using VBox (column)
		VBox titleBox = new VBox();
		titleBox.setAlignment(Pos.CENTER_RIGHT); // Right align Title
		titleBox.getChildren().addAll(title); // Add Title

		// Display quit button using VBox (column)
		VBox quitBox = new VBox();
		quitBox.setAlignment(Pos.BOTTOM_RIGHT);
		quitBox.getChildren().addAll(quitBtn);

		// Set alignment for VBoxes
		borderPane.setLeft(buttonsBox);
		borderPane.setRight(titleBox);
		borderPane.setBottom(quitBox);

		// Apply CSS Sheet
		applyCSS(mainScene);

		// Display the Scene
		primaryStage.setScene(mainScene);
		primaryStage.show();
	} // runMainMenu

	/**
	 * Creates and displays data entry (add friends and add friendships) screens
	 * Based on sceneType, will add a friend given one user or a friendship given two users
	 * Once a friend or friendship has been add, an alert will display to update the user
	 * In addFriendship, if user has not been added, user will be added and then friendship
	 * will be created. An error will display if a friend or friendship failed to be added
	 * 
	 * @param primaryStage for display
	 * @param sceneType label and action for screen, Friend or Friendship
	 * 					used to determine whether to prompt for one user or two users
	 */
	private void runDataEntryMenu(Stage primaryStage, String sceneType) {
		// Screen label, TextField, and button setup
		Label screenPrompt = new Label("Add " + sceneType); // ScreenLabel
		screenPrompt.setId("header");
		Label addFriendPrompt = new Label("Enter username to add an individual: ");
		Label addFriendshipPrompt1 = new Label("Enter first username to add to a friendship: ");
		Label addFriendshipPrompt2 = new Label("Enter second username to add the friendship: ");
		TextField username1 = new TextField();
		TextField username2 = new TextField();
		Button add = new Button("Add");
		add.setId("addBtn");

		// Scene setup
		BorderPane entryPane = new BorderPane();
		Scene entryScene = new Scene(entryPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		entryPane.setId("borderPane"); // IS THIS COOL TO REUSE?

		// Combine the addFriend/FriendshipPrompt(s) and Button using HBox (row)
		HBox addPrompt1 = new HBox();
		HBox addPrompt2 = new HBox();
		addPrompt1.setSpacing(8);
		addPrompt2.setSpacing(8);
		addPrompt1.setAlignment(Pos.CENTER_LEFT);
		addPrompt2.setAlignment(Pos.CENTER_LEFT);

		// Combine screen label and the addPrompts HBox(es) to VBox (column)
		VBox menuPrompts = new VBox();
		menuPrompts.setSpacing(8);
		// if adding friend, only use addPrompt1
		if (sceneType == "Friend") {
			add.setOnAction(e -> {
				boolean userAdded = socialNetwork.addFriend(username1.getText());
				if (userAdded) {
					userAlert(username1.getText(), 'a');
				} else {
					errorAlert();
				}
				runDataEntryMenu(primaryStage, sceneType);
			});
			addPrompt1.getChildren().addAll(addFriendPrompt, username1, add);
			menuPrompts.getChildren().addAll(screenPrompt, addPrompt1);
		} 
		// if adding friendship, use both addPrompt1 and addPrompt2
		else if (sceneType == "Friendship") {
			add.setOnAction(e -> {
				boolean friendshipAdded = socialNetwork.addFriendship(username1.getText(), username2.getText());
				if (friendshipAdded) {
					friendshipAlert(username1.getText(), username2.getText(), 'a');
				} else {
					errorAlert();
				}
				runDataEntryMenu(primaryStage, sceneType);
			});
			addPrompt1.getChildren().addAll(addFriendshipPrompt1, username1);
			addPrompt2.getChildren().addAll(addFriendshipPrompt2, username2, add);
			menuPrompts.getChildren().addAll(screenPrompt, addPrompt1, addPrompt2);
		}
		menuPrompts.setAlignment(Pos.CENTER_LEFT);
		menuPrompts.setPadding(new Insets(0, 0, 0, 15));
		entryPane.setCenter(menuPrompts);

		// Return to main menu
		HBox bottomSection = new HBox();
		bottomSection.getChildren().addAll(back);
		entryPane.setBottom(bottomSection);

		// Apply styling
		applyCSS(entryScene);

		// Display the Scene
		primaryStage.setScene(entryScene);
		primaryStage.show();
	} // runDataEntryMenu

	/**
	 * Creates and displays data removal (remove friends and remove friendships) screens
	 * Based on sceneType, will remove a friend given one user or a friendship given two users
	 * Once a friend or friendship has been removed, an alert will display to update the user
	 * An error will display if a friend or friendship failed to be removed
	 * 
	 * @param primaryStage for display
	 * @param sceneType label and action for screen, Friend or Friendship
	 * 					used to determine whether to prompt for one user or two users
	 */
	private void runDataRemovalMenu(Stage primaryStage, String sceneType) {
		// Screen label, TextField, and button setup
		Label menuLabel = new Label("Remove " + sceneType); // ScreenLabel
		menuLabel.setId("header");
		Label removeFriendPrompt = new Label("Enter username to remove an individual from network: ");
		Label removeFriendshipPrompt1 = new Label("Enter first username to remove from a friendship: ");
		Label removeFriendshipPrompt2 = new Label("Enter second username to remove the friendship: ");
		TextField username1 = new TextField();
		TextField username2 = new TextField();
		Button delete = new Button("Delete");
		delete.setId("deleteBtn");

		// Scene setup
		BorderPane entryPane = new BorderPane();
		Scene entryScene = new Scene(entryPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		entryPane.setId("borderPane");

		// Combine the removeFriend/FriendshipPrompt(s) and Button using HBox (row)
		HBox removePrompt1 = new HBox(); // used for both remove friend and remove friendship
		HBox removePrompt2 = new HBox(); // only used for remove friendship
		removePrompt1.setSpacing(8);
		removePrompt2.setSpacing(8);
		removePrompt1.setAlignment(Pos.CENTER_LEFT);
		removePrompt2.setAlignment(Pos.CENTER_LEFT);

		// Combine screen label and the removePrompts HBox(es) to VBox (column)
		VBox menuPrompts = new VBox();
		menuPrompts.setSpacing(8);
		// if removing friend, only use removePrompt1
		if (sceneType == "Friend") {
			delete.setOnAction(e -> {
				boolean userRemoved = socialNetwork.removeFriend(username1.getText());
				if (userRemoved) {
					userAlert(username1.getText(), 'r');
				} else {
					errorAlert();
				}
				runDataRemovalMenu(primaryStage, sceneType);
			});
			removePrompt1.getChildren().addAll(removeFriendPrompt, username1, delete);
			menuPrompts.getChildren().addAll(menuLabel, removePrompt1);
		} 
		// if removing friendship, use both removePrompt1 and removePrompt2
		else if (sceneType == "Friendship") {
			delete.setOnAction(e -> {
				boolean friendshipRemoved = socialNetwork.removeFriendship(username1.getText(), username2.getText());
				if (friendshipRemoved) {
					friendshipAlert(username1.getText(), username2.getText(), 'r');
				} else {
					errorAlert();
				}
				runDataRemovalMenu(primaryStage, sceneType);
			});
			removePrompt1.getChildren().addAll(removeFriendshipPrompt1, username1);
			removePrompt2.getChildren().addAll(removeFriendshipPrompt2, username2, delete);
			menuPrompts.getChildren().addAll(menuLabel, removePrompt1, removePrompt2);
		}
		menuPrompts.setAlignment(Pos.CENTER_LEFT);
		menuPrompts.setPadding(new Insets(0, 0, 0, 15));
		entryPane.setCenter(menuPrompts);

		// Return to main menu
		HBox bottomSection = new HBox();
		bottomSection.getChildren().addAll(back);
		entryPane.setBottom(bottomSection);

		// Apply styling
		applyCSS(entryScene);

		// Display the Scene
		primaryStage.setScene(entryScene);
		primaryStage.show();
	} // runDataRemovalMenu

	/**
	 * Creates and displays the Clear Network screen. If the program user clears the network,
	 * a alert will prompt warning the user that all friends and friendships will be removed
	 * 
	 * @param primaryStage for display
	 */
	private void runClearNetworkMenu(Stage primaryStage) {
		// Screen label and button setup
		Label menuLabel = new Label("Clear Network"); // ScreenLabel
		menuLabel.setId("header");
		Label clearNetworkPrompt = new Label(
				"Do you want to clear the network? This will clear all friends and all friendships from the network.");
		Button clearButton = new Button("Clear");
		clearButton.setId("deleteBtn");
		clearButton.setOnAction(e -> clearNetworkConfirmationAlert(primaryStage));

		// Scene setup
		BorderPane entryPane = new BorderPane();
		Scene entryScene = new Scene(entryPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		entryPane.setId("borderPane");

		// Combine the clearNetworkPrompt and Button using HBox (row)
		HBox individualPrompt = new HBox();
		individualPrompt.setSpacing(8);
		individualPrompt.setAlignment(Pos.CENTER_LEFT);
		individualPrompt.getChildren().addAll(clearNetworkPrompt, clearButton);

		// Combine screen label and the clearNetwork HBox to VBox (column)
		VBox menuPrompts = new VBox();
		menuPrompts.getChildren().addAll(menuLabel, individualPrompt);
		menuPrompts.setAlignment(Pos.CENTER_LEFT);
		menuPrompts.setPadding(new Insets(0, 0, 0, 15));
		entryPane.setCenter(menuPrompts);

		// Return to main menu
		HBox bottomSection = new HBox();
		bottomSection.getChildren().addAll(back);
		entryPane.setBottom(bottomSection);

		// Apply styling
		applyCSS(entryScene);

		// Display the Scene
		primaryStage.setScene(entryScene);
		primaryStage.show();
	} // runClearNetworkMenu

	/**
	 * Creates and displays data output screen from the perspective of the central
	 * user. Displays a status/error report and allows program user to find mutual
	 * friends of a person, find the closest connection between two people, or find
	 * the number of connections in the Social Network. Also displays the most recent
	 * actions and errors
	 * 
	 * @param primaryStage for display
	 */
	private void runDisplayInteractMenu(Stage primaryStage) {
		// Label and button setup
		Label centralUserLabel = new Label();
		Button centralUserButton = new Button();
		centralUserButton.setId("centralUserBtn");
		centralUserButton.setAlignment(Pos.BASELINE_LEFT);
		Label centralUserFriendsLabel = new Label();
		Label statusReportHeader = new Label("Recent Actions:");
		statusReportHeader.setFont(Font.font(null, FontWeight.BOLD, 14));
		Label errorReportHeader = new Label("Recent Errors:");
		errorReportHeader.setFont(Font.font(null, FontWeight.BOLD, 14));
		Label additionalOptions = new Label("Additional Options:");
		additionalOptions.setFont(Font.font(null, FontWeight.BOLD, 14));
		Button findMutualFriends = new Button("Mutual Friends");
		findMutualFriends.setOnAction(e -> runMutualFriendsMenu(primaryStage));
		Button findClosestConnection = new Button("Closest Connection");
		Button findNumOfConnections = new Button("Num Connections");
		Label numUsers = new Label("Number of Users: " + socialNetwork.getNumPeople());
		numUsers.setFont(Font.font(null, FontWeight.BOLD, 14));
		Label numFriendships = new Label("Number of Friendships: " + socialNetwork.getNumConnections());
		numFriendships.setFont(Font.font(null, FontWeight.BOLD, 14));
		
		// Scene setup
		GridPane displayGridPane = new GridPane();
		// Setup row and column constraints to make it easier to place HBox(es) and VBox(es)
		for (int i = 0; i < 60; i++) {
			ColumnConstraints column = new ColumnConstraints(10);
			displayGridPane.getColumnConstraints().add(column);
		}
		for (int i = 0; i < 45; i++) {
			RowConstraints row = new RowConstraints(10);
			displayGridPane.getRowConstraints().add(row);
		}
		displayGridPane.setId("borderPane");
		Scene displayScene = new Scene(displayGridPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// Combine central user label and central user button using VBox (column)
		VBox centralUserDisplay = new VBox(); // used for central user
		if (socialNetwork.getCentralUser() != null) {
			centralUserLabel = new Label("Central User:");
			centralUserLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
			centralUserDisplay.getChildren().add(centralUserLabel);
			centralUserButton = new Button(socialNetwork.getCentralUser().getUsername());
			centralUserButton.setId("centralUserBtn");
			centralUserButton.setAlignment(Pos.BASELINE_LEFT); // Align text in button to the left
			centralUserDisplay.getChildren().add(centralUserButton);
		} else {
			centralUserLabel = new Label("SocialNetwork is empty");
			centralUserLabel.setId("displayHeader");
			centralUserLabel.setAlignment(Pos.BASELINE_LEFT); // Align text in button to the left
			centralUserDisplay.getChildren().add(centralUserLabel);
		}
		
		// Combine central user friends label and ListView that allows program user to set new central user using VBox (column)
		VBox centralUserFriendsDisplay = new VBox();
		if (socialNetwork.getCentralUser() != null) {
			centralUserFriendsLabel = new Label(socialNetwork.getCentralUser().getUsername() + "'s friends:");
			centralUserFriendsLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
			centralUserFriendsDisplay.getChildren().add(centralUserFriendsLabel);
			List<String> centUserFriends = socialNetwork.getCentralUserFriends();
			ObservableList<String> tempCentUserFriendsList = FXCollections.observableArrayList(centUserFriends);
			ListView<String> centFriendsListView = new ListView<String>(tempCentUserFriendsList);
			centFriendsListView.setMaxHeight((WINDOW_HEIGHT / 2) + 35);
			centFriendsListView.setMaxWidth(WINDOW_WIDTH / 4);
			centFriendsListView.setOnMouseClicked(e -> {
				socialNetwork.setCentralUser(centFriendsListView.getSelectionModel().getSelectedItem());
				if (centFriendsListView.getSelectionModel().getSelectedItem() == null) {
					errorAlert();
				}
				runDisplayInteractMenu(primaryStage);
			});
			centralUserFriendsDisplay.getChildren().add(centFriendsListView);
		}
		
		// Combine status report label and ListView that displays most recent actions using VBox (column)
		VBox statusReportDisplay = new VBox();
		List<String> logList = socialNetwork.getLog();
		ObservableList<String> logObservList = FXCollections.observableArrayList(logList);
		ListView<String> logListView = new ListView<String>(logObservList);
		logListView.setMaxHeight(WINDOW_HEIGHT / 3);
		logListView.setMaxWidth(WINDOW_WIDTH / 4);
		statusReportDisplay.getChildren().add(statusReportHeader);
		statusReportDisplay.getChildren().add(logListView);
		statusReportDisplay.setAlignment(Pos.TOP_LEFT);
		
		// Combine status report label and ListView that displays most recent errors using VBox (column)
		VBox errorReportDisplay = new VBox();
		errorReportDisplay.getChildren().add(errorReportHeader);
		errorReportDisplay.setAlignment(Pos.TOP_LEFT);
		List<String> errorLogList = socialNetwork.getErrorLog();
		ObservableList<String> errorLogObservList = FXCollections.observableArrayList(errorLogList);
		ListView<String> errorLogListView = new ListView<String>(errorLogObservList);
		errorLogListView.setMaxHeight(WINDOW_HEIGHT / 3);
		// errorLogListView.setMaxWidth(WINDOW_WIDTH / 2);
		errorReportDisplay.getChildren().add(errorLogListView);
		
		// Used to allow the user to find mutual friends, find closest connection between two user, 
		// or the number of connections in network using VBox (column)
		VBox additionalActionsDisplay = new VBox();
		additionalActionsDisplay.getChildren().addAll(additionalOptions, findMutualFriends, findClosestConnection, findNumOfConnections);
		additionalActionsDisplay.setAlignment(Pos.TOP_LEFT);
		
		// Return to main menu
		HBox backBottomSection = new HBox();
		backBottomSection.getChildren().add(back);
		backBottomSection.setAlignment(Pos.BOTTOM_LEFT);
		
		// Displays number of users in network
		HBox bottomNumUsersSection = new HBox();
		bottomNumUsersSection.setAlignment(Pos.CENTER_LEFT);
		bottomNumUsersSection.getChildren().add(numUsers);
		
		// Displays number of friendships in network
		HBox bottomNumFriendshipsSection = new HBox();
		bottomNumFriendshipsSection.setAlignment(Pos.CENTER_LEFT);
		bottomNumFriendshipsSection.getChildren().add(numFriendships);

		// Put everything together
		displayGridPane.add(centralUserDisplay, 0, 0, 19, 10);
		if ((socialNetwork.getCentralUser() != null)) {
			displayGridPane.add(centralUserFriendsDisplay, 1, 9, 19, 39);
		}
		displayGridPane.add(statusReportDisplay, 20, 0, 19, 19);
		displayGridPane.add(errorReportDisplay, 20, 20, 34, 19);
		displayGridPane.add(additionalActionsDisplay, 40, 5, 19, 10);
		displayGridPane.add(backBottomSection, 0, 35, 10, 10);
		displayGridPane.add(bottomNumUsersSection, 20, 39, 19, 10);
		displayGridPane.add(bottomNumFriendshipsSection, 40, 39, 19, 10);
		
		// Apply styling
		applyCSS(displayScene);

		// Display the scene
		primaryStage.setScene(displayScene);
		primaryStage.show();
	} // runDisplayInteractMenu

	/**
	 * Creates and displays a screen used to find the mutual friends of two users in the network
	 * If Find button is selected, menu jumps to mutualFriends alert that will display an alert to the 
	 * user that shows the mutual friends or the reason why the mutual friends were not found
	 * @param primaryStage for display
	 */
	private void runMutualFriendsMenu(Stage primaryStage) {
		// Screen label, TextField, and button setup
		Label menuLabel = new Label("Find Mutual Friends"); // Top screen label for what option the user selected
		menuLabel.setId("header");
		Label friendPrompt1 = new Label("Enter first username to find mutual friends for: ");
		Label friendPrompt2 = new Label("Enter username to find mutual friends of first username: ");
		TextField username1 = new TextField();
		TextField username2 = new TextField();
		Button findFriendsButton = new Button("Find");
		findFriendsButton.setId("addBtn");
		findFriendsButton.setOnAction(e -> mutualFriendsAlert(primaryStage, username1.getText(), username2.getText()));

		// Scene setup
		BorderPane entryPane = new BorderPane();
		Scene entryScene = new Scene(entryPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		entryPane.setId("borderPane");

		// Combine the friendPrompts and TextFields using HBox (row)
		HBox individualPrompt1 = new HBox();
		HBox individualPrompt2 = new HBox();
		individualPrompt1.setSpacing(8);
		individualPrompt2.setSpacing(8);
		individualPrompt1.setAlignment(Pos.CENTER_LEFT);
		individualPrompt2.setAlignment(Pos.CENTER_LEFT);
		individualPrompt1.getChildren().addAll(friendPrompt1, username1);
		individualPrompt2.getChildren().addAll(friendPrompt2, username2, findFriendsButton);

		// Combine screen label and individual prompts HBox to VBox (column)
		VBox menuPrompts = new VBox();
		menuPrompts.getChildren().addAll(menuLabel, individualPrompt1, individualPrompt2);
		menuPrompts.setAlignment(Pos.CENTER_LEFT);
		menuPrompts.setPadding(new Insets(0, 0, 0, 15));
		entryPane.setCenter(menuPrompts);

		// Return to main menu
		HBox bottomSection = new HBox();
		bottomSection.getChildren().addAll(back);
		entryPane.setBottom(bottomSection);

		// Apply styling
		applyCSS(entryScene);

		// Display the Scene
		primaryStage.setScene(entryScene);
		primaryStage.show();
	} // runMutualFriendsMenu

	/**
	 * Menu used to allow the user to import a file with a filepath
	 * file must be in the correct format to be imported successfully
	 * if imported successfully, will jump user to display/interact screen
	 * @param primaryStage for display
	 */
	private void runImportMenu(Stage primaryStage) {
		// Screen label, Textfield, and button setup
		Label menuLabel = new Label("Import");
		menuLabel.setId("header");
		Label directions = new Label(
				"Enter a file to quickly add/remove users, add/remove friendships, or set a central user.");
		Label filePrompt = new Label("File path: ");
		Button importDataButton = new Button("Import");
		importDataButton.setId("importBtn");
		TextField fileEntry = new TextField(); // Accepts the file path
		
		// Define action for importDataButton
		importDataButton.setOnAction(e -> {
			boolean imported = socialNetwork.buildGraph(fileEntry.getText());
			if (imported) {
				importAlert(fileEntry.getText());
				runDisplayInteractMenu(primaryStage); // return to display/interact screen
			} else {
				errorAlert();
			}
		});

		// Scene setup
		BorderPane entryPane = new BorderPane();
		Scene entryScene = new Scene(entryPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		entryPane.setId("borderPane");

		// Combine the filePrompt, input field, and import action to a HBox (row)
		HBox importRow = new HBox();
		importRow.setSpacing(8);
		importRow.getChildren().addAll(filePrompt, fileEntry, importDataButton);
		importRow.setAlignment(Pos.CENTER_LEFT);

		// Combine screen label, directions, and importRow HBox to VBox (column)
		VBox menuPrompts = new VBox();
		menuPrompts.setSpacing(8);
		menuPrompts.getChildren().addAll(menuLabel, directions, importRow);
		menuPrompts.setAlignment(Pos.CENTER_LEFT);
		menuPrompts.setPadding(new Insets(0, 0, 0, 15));
		entryPane.setCenter(menuPrompts);

		// Return to main menu
		HBox bottomSection = new HBox();
		bottomSection.getChildren().addAll(back);
		entryPane.setBottom(bottomSection);

		// Apply Styling
		applyCSS(entryScene);

		// Display the Scene
		primaryStage.setScene(entryScene);
		primaryStage.show();
	} // runImportMenu

	/**
	 * Menu for when the user is about to exit the program. Allows user to leave without saving
	 * or saving actions to a file named log.txt
	 *
	 * @param primaryStage for display
	 */
	private void runExitMenu(Stage primaryStage) {
		// Screen label and button setup
		Label menuLabel = new Label("Would you like to save to file before exiting?");
		menuLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
		Button save = new Button("Save");
		Button exit = new Button("Exit without Save");
		
		// Scene setup
		BorderPane entryPane = new BorderPane();
		Scene entryScene = new Scene(entryPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		entryPane.setId("borderPane");
		
		// Combine the save and exit without saving buttons using HBox (row)
		HBox saveDontSaveButtons = new HBox();
		saveDontSaveButtons.setAlignment(Pos.CENTER);
		saveDontSaveButtons.getChildren().addAll(save, exit);
		exit.setOnAction(e -> exitWithoutSaveAlert());
		save.setOnAction(e -> {
			socialNetwork.printLogFile();
			goodbyeAlert("save");
		});
		
		// Combine screen label and the saveDontSaveButtons HBox to VBox (column)
		VBox menuPrompts = new VBox();
		menuPrompts.getChildren().addAll(menuLabel, saveDontSaveButtons);
		menuPrompts.setAlignment(Pos.CENTER);
		entryPane.setCenter(menuPrompts);

		// Buttons
		HBox bottomSection = new HBox();
		bottomSection.getChildren().addAll(back);
		entryPane.setBottom(bottomSection);

		// Apply styling
		applyCSS(entryScene);

		// Display the Scene
		primaryStage.setScene(entryScene);
		primaryStage.show();
	} // runExitMenu

	/**
	 * Confirmation Alert box that triggers when the user wishes to exit the program without
	 * saving
	 */
	private void exitWithoutSaveAlert() {
		ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.NO);

		// Remove default Buttons and add new buttons
		Alert exitWithoutSaveAlert = new Alert(Alert.AlertType.CONFIRMATION);
		exitWithoutSaveAlert.setHeaderText("Are you sure you want to exit without saving?");
		exitWithoutSaveAlert.getButtonTypes().clear();
		exitWithoutSaveAlert.getButtonTypes().addAll(yes, cancel);

		// showAndWait returns an Optional containing the button that was pressed
		Optional<ButtonType> result = exitWithoutSaveAlert.showAndWait();

		if (result.get() == yes) {
			goodbyeAlert("quit");
		}
	} // exitWithoutSaveAlert

	/**
	 * Alert that first when a user quits from the program
	 * @param action determines whether to display exiting or saving when leaving program
	 */
	private void goodbyeAlert(String action) {
		Alert goodbyeAlert = new Alert(Alert.AlertType.NONE);
		
		if (action == "quit") {
			goodbyeAlert.setHeaderText("Exiting...");
		}
		if (action == "save") {
			goodbyeAlert.setHeaderText("Saving...");
		}
		goodbyeAlert.show();

		// Pause before displaying goodbye message
		PauseTransition delay = new PauseTransition(Duration.millis(500));
		delay.setOnFinished(e -> goodbyeAlert.setContentText("Goodbye!"));
		delay.play();

		// Pause before exiting program
		PauseTransition exitDelay = new PauseTransition(Duration.millis(1000));
		exitDelay.setOnFinished(e -> Platform.exit());
		exitDelay.play();
	} // goodbyeAlert

	/**
	 * Alert that a user has either been successfully added or removed from network depending on 
	 * the action
	 * @param username the user added or removed from the network
	 * @param action 'a' for added and 'r' for removed
	 */
	private void userAlert(String username, char action) {
		Alert userAlert = new Alert(Alert.AlertType.INFORMATION);
		if (action == 'a') {
			userAlert.setHeaderText("User added");
			userAlert.setContentText(username + " has been added to the network!");
		}
		if (action == 'r') {
			userAlert.setHeaderText("User removed");
			userAlert.setContentText(username + " has been removed from the network!");
		}
		userAlert.show();
	} // userAlert

	/**
	 * Alert that a friendship has either been successfully added or removed depending on 
	 * the action
	 * @param username1 first user that has had a friendship added or removed
	 * @param username2 user whose friendship was added or removed to username1
	 * @param action 'a' for added and 'r' for removed
	 */
	private void friendshipAlert(String username1, String username2, char action) {
		Alert friendshipAlert = new Alert(Alert.AlertType.INFORMATION);
		if (action == 'a') {
			friendshipAlert.setHeaderText("Friendship added");
			friendshipAlert.setContentText(username1 + " and " + username2 + " are now friends!");
		}
		if (action == 'r') {
			friendshipAlert.setHeaderText("Friendship removed");
			friendshipAlert.setContentText(username1 + " and " + username2 + " are no longer friends!");
		}
		friendshipAlert.show();
	} // friendshipAlert

	/**
	 * Warning alert to display that an error has occurred for an action
	 */
	private void errorAlert() {
		Alert errorAlert = new Alert(Alert.AlertType.WARNING);
		errorAlert.setContentText(socialNetwork.getLatestError());
		errorAlert.show();
	} // errorAlert

	/**
	 * Confirmation Alert that the user wants to clear the network
	 * If the user selects yes, the network will be cleared and will
	 * return to the main menu
	 * @param primaryStage for display
	 */
	private void clearNetworkConfirmationAlert(Stage primaryStage) {
		ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.NO);

		// Remove default Buttons and add new buttons
		Alert clearNetworkConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
		clearNetworkConfirmationAlert.setHeaderText("Are you sure you want to clear the network?");
		clearNetworkConfirmationAlert.getButtonTypes().clear();
		clearNetworkConfirmationAlert.getButtonTypes().addAll(yes, cancel);

		// showAndWait returns an Optional containing the button that was pressed
		Optional<ButtonType> result = clearNetworkConfirmationAlert.showAndWait();

		if (result.get() == yes) {
			socialNetwork.clearNetwork(); // clear network
			clearNetworkAlert();
			runMainMenu(primaryStage);
		}
	} // clearNetworkConfirmationAlert


	/**
	 * Alert box to tell the user that the network has been cleared
	 */
	
	private void clearNetworkAlert() {
		Alert clearNetworkAlert = new Alert(Alert.AlertType.INFORMATION);
		clearNetworkAlert.setHeaderText("Network cleared");
		clearNetworkAlert.setContentText("Network has been cleared! All vertices and connections have been removed.");
		clearNetworkAlert.show();
	} // clearNetworkAlert

	/**
	 * Alert box to tell the user of mutual friends of the two inputted usernames
	 * @param primaryStage for display
	 * @param username1 user to find mutual friends for
	 * @param username2 user to find mutual friends with username1
	 */
	private void mutualFriendsAlert(Stage primaryStage, String username1, String username2) {
		Alert mutualFriendsAlert = new Alert(Alert.AlertType.INFORMATION);
		mutualFriendsAlert.setHeaderText(username1 + " and " + username2 + " have mutual friends");
		
		List<String> mutualFriendsList = socialNetwork.mutualFriends(username1, username2);
		// StringBuilder used to append list of mutual friends
		StringBuilder mutualFriendsBuilder = new StringBuilder();

		// if a single mutual friend, display unique alert
		if (mutualFriendsList.size() == 1) {
			mutualFriendsAlert.setContentText(mutualFriendsList.get(0).toString() + " is a mutual friend of "
					+ username1 + " and " + username2 + "!");
		} else {
			for (int i = 0; i < mutualFriendsList.size() - 1; i++) {
				// if only two mutual friends, don't add comma between the two mutual users
				if (mutualFriendsList.size() == 2) {
					mutualFriendsBuilder.append(mutualFriendsList.get(i).toString() + " ");
				} 
				// if more than two mutual friends, display mutual friends separated by commas
				else {
					mutualFriendsBuilder.append(mutualFriendsList.get(i).toString() + ", ");
				}
			}
			mutualFriendsBuilder.append("and " + mutualFriendsList.get(mutualFriendsList.size() - 1).toString());
			mutualFriendsBuilder.append(" are mutual friends of " + username1 + " and " + username2 + "!");
			String mutualFriends = mutualFriendsBuilder.toString();
			mutualFriendsAlert.setContentText(mutualFriends);
		}

		mutualFriendsAlert.show();
		
		// after alert is shown, return to MutualFriendsMenu
		runMutualFriendsMenu(primaryStage);

	} // mutualFriendsAlert

	/**
	 * Alert box to tell the user the import was successful
	 * @param filename the filename that was imported successfully
	 */
	private void importAlert(String filename) {
		Alert importAlert = new Alert(Alert.AlertType.INFORMATION);
		importAlert.setHeaderText("Import Successful");
		importAlert.setContentText("The import of " + filename + " has been completed!");
		importAlert.show();
	} // importAlert

	/**
	 * Applies CSS styling to selected scene. Stylesheet is defined in static
	 * variable.
	 *
	 * @param scene scene to style
	 */
	private void applyCSS(Scene scene) {
		scene.getStylesheets().add(getClass().getResource(styleSheet).toExternalForm());
	} // applyCSS

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		back.setId("backBtn");
		launch(args);
	} // main
}