package org.openjfx.gamebox;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaderboardPageController {

    @FXML
    private TableView<UserScore> leaderboardTable;
    @FXML
    private TableColumn<UserScore, String> nameColumn;
    @FXML
    private TableColumn<UserScore, Integer> tileGameBestTimeScoreColumn, chessScoreColumn, spaceInvadersScoreColumn, swimmingScoreColumn, tileGameScoreColumn;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        tileGameBestTimeScoreColumn.setCellValueFactory(new PropertyValueFactory<>("tileGameBestTimeScore"));
        chessScoreColumn.setCellValueFactory(new PropertyValueFactory<>("chessScore"));
        spaceInvadersScoreColumn.setCellValueFactory(new PropertyValueFactory<>("spaceInvadersScore"));
        swimmingScoreColumn.setCellValueFactory(new PropertyValueFactory<>("swimmingScore"));
        loadScores();
    }

    private void loadScores() {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("users")
                .orderBy("TileGameBestTimeScore")
                .get();

        Task<Void> firestoreTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                QuerySnapshot querySnapshot = future.get();
                Platform.runLater(() -> {
                    querySnapshot.getDocuments().forEach(document -> {
                        UserScore userScore = new UserScore(
                                document.getString("displayName"),
                                parseToInt(document, "TileGameBestTimeScore"),
                                parseToInt(document, "ChessScore"),
                                parseToInt(document, "SpaceInvadersScore"),
                                parseToInt(document, "SwimmingScoreScore"),
                                parseToInt(document, "TileGameScore")
                        );
                        leaderboardTable.getItems().add(userScore);
                    });
                });
                return null;
            }
        };
        executorService.submit(firestoreTask);
    }

    // Helper method to parse integers from Firestore documents, handling possible string values
    private Integer parseToInt(DocumentSnapshot document, String field) {
        Object value = document.get(field);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse integer from string for field " + field + ": " + value);
                return null;  // Or handle this case as you see fit
            }
        }
        return null;
    }
    public static class UserScore {
        private String displayName;
        private Integer tileGameBestTimeScore;
        private Integer chessScore;
        private Integer spaceInvadersScore;
        private Integer swimmingScore;
        private Integer tileGameScore;

        public UserScore(String displayName, Integer tileGameBestTimeScore, Integer chessScore, Integer spaceInvadersScore, Integer swimmingScore, Integer tileGameScore) {
            this.displayName = displayName;
            this.tileGameBestTimeScore = tileGameBestTimeScore;
            this.chessScore = chessScore;
            this.spaceInvadersScore = spaceInvadersScore;
            this.swimmingScore = swimmingScore;
            this.tileGameScore = tileGameScore;
        }

        public String getDisplayName() { return displayName; }
        public Integer getTileGameBestTimeScore() { return tileGameBestTimeScore; }
        public Integer getChessScore() { return chessScore; }
        public Integer getSpaceInvadersScore() { return spaceInvadersScore; }
        public Integer getSwimmingScore() { return swimmingScore; }
        public Integer getTileGameScore() { return tileGameScore; }
    }

    public void shutdown() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
