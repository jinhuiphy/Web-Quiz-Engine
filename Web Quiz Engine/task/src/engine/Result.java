package engine;

public class Result {
    public static Result CORRECT = new Result(true, "Congratulations, you're right!");
    public static Result WRONG = new Result(false, "Wrong answer! Please, try again.");

    private boolean success;
    private String feedback;

    public Result(boolean success, String feedback) {
        this.success = success;
        this.feedback = feedback;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
