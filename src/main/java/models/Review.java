package models;

import java.time.LocalDateTime;

public class Review {

    private String writtenBy;
    private int rating;
    private LocalDateTime createdAt;
    private int id;
    private int restaurantId;

    public Review(String writtenBy, int rating, int restaurantId) {
        this.writtenBy = writtenBy;
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (rating != review.rating) return false;
        if (id != review.id) return false;
        return writtenBy.equals(review.writtenBy);
    }

    @Override
    public int hashCode() {
        int result = writtenBy.hashCode();
        result = 31 * result + rating;
        result = 31 * result + id;
        return result;
    }
}
