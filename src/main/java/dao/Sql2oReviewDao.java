package dao;

import models.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
public class Sql2oReviewDao implements ReviewDao {

    private final Sql2o sql2o;

    public Sql2oReviewDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public void add(Review review) {
        String sql = "INSERT INTO reviews (writtenby, rating, content, restaurantid, createdat) VALUES (:writtenBy, :rating, :content, :restaurantId, :createdat)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(review)
                    .executeUpdate()
                    .getKey();
            review.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE from reviews WHERE id=:id"; //raw sql
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public List<Review> getAllReviewsByRestaurant(int restaurantId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM reviews WHERE restaurantId = :restaurantId")
                    .addParameter("restaurantId", restaurantId)
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM reviews")
                    .executeAndFetch(Review.class);
        }
    }

    @Override
    public List<Review> getAllReviewsByRestaurantSortedNewestToOldest(int restaurantId) {
        List<Review> unsortedReviews = getAllReviewsByRestaurant(restaurantId);
        List<Review> sortedReviews = new ArrayList<>();

        for (int i = 0; i < unsortedReviews.size()-1; i++) {
            int comparisonResult = unsortedReviews.get(i).compareTo(unsortedReviews.get(i + 1));
            if (comparisonResult == -1) { //first object was made earlier than second object
                sortedReviews.add(0,unsortedReviews.get(i + 1)); //push the second object to the top of the list.
            } else if (comparisonResult == 0) {
                sortedReviews.add(0,unsortedReviews.get(i)); //probably should have a tie breaker here as they are the same.
            } else {
                sortedReviews.add(0,unsortedReviews.get(i)); //push the first object to the list as it is newer than the second object.
            }
        }

    return sortedReviews;
    }
}
