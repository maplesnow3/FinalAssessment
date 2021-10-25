package comp5216.finalAssessment;

public class Comment {
    private String comment;
    private int rating;
    private String postedBy;

    public Comment(String comment,int rating,String postedBy){
        this.comment = comment;
        this.rating = rating;
        this.postedBy = postedBy;
    }

    public int getRating() {
        return rating;
    }

    public String getComment(){
        return  comment;
    }

    public String getPostedBy() {
        return postedBy;
    }
}
