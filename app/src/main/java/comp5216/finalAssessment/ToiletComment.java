package comp5216.finalAssessment;

public class ToiletComment {
    private String comment;
    private int rating;
    private String postedAt;

    public ToiletComment(String comment, int rating, String postedAt){
        this.comment = comment;
        this.rating = rating;
        this.postedAt = postedAt;
    }

    public int getRating() {
        return rating;
    }

    public String getComment(){
        return  comment;
    }

    public String getPostedAt() {
        return postedAt;
    }
}
