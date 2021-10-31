package comp5216.finalAssessment;

public class Comment {

    public Comment(){

    }

    public int  commentId;
    public  String content;
    public int rating;
    public String submitTime;
    public int  toiletId;
    public String toiletName;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public int getToiletId() {
        return toiletId;
    }

    public void setToiletId(int toiletId) {
        this.toiletId = toiletId;
    }

    public String getToiletName() {
        return toiletName;
    }

    public void setToiletName(String toiletName) {
        this.toiletName = toiletName;
    }


}