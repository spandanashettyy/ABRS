import org.apache.mahout.cf.taste.eval.*;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.

import java.io.File;
import java.util.List;

public class RecommendationSystem {

    public static void main(String[] args) {
        try {
            // Load the dataset
            File dataFile = new File("data/ratings.csv"); // Path to your dataset
            DataModel model = new FileDataModel(dataFile);

            // User-based Recommendation
            System.out.println("User-based Recommendations:");
            userBasedRecommendation(model);

            // Item-based Recommendation
            System.out.println("\nItem-based Recommendations:");
            itemBasedRecommendation(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void userBasedRecommendation(DataModel model) throws Exception {
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        for (LongPrimitiveIterator users = model.getUserIDs(); users.hasNext(); ) {
            long userId = users.nextLong();
            List<RecommendedItem> recommendations = recommender.recommend(userId, 3);

            System.out.println("User ID: " + userId);
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("  Item ID: " + recommendation.getItemID() + ", Value: " + recommendation.getValue());
            }
        }
    }

    private static void itemBasedRecommendation(DataModel model) throws Exception {
        ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
        Recommender recommender = new GenericItemBasedRecommender(model, similarity);

        for (LongPrimitiveIterator items = model.getItemIDs(); items.hasNext(); ) {
            long itemId = items.nextLong();
            List<RecommendedItem> recommendations = recommender.recommend(itemId, 3);

            System.out.println("Item ID: " + itemId);
            for (RecommendedItem recommendation : recommendations) {
                System.out.println("  Recommended Item ID: " + recommendation.getItemID() + ", Value: " + recommendation.getValue());
            }
        }
    }
}