package oop.socket;

public record ProposedEvent(boolean isProposedActivity, String activityName, String activityDescription, int year, int month, int day) {
    @Override
    public final String toString() {
        return String.format(" %s|%s|%d.%d.%d.", activityName, activityDescription, day, month, year);
        // if(year != 0) {
        //     return String.format("aktivnost: %s\nopis: %s\ndatum:%d.%d.%d.", activityName, activityDescription, day, month, year);
        // } else {
        //     return String.format("aktivnost: %s\nopis: %s", activityName, activityDescription);
        // }
    }
}