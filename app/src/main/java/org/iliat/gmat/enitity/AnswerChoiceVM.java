package org.iliat.gmat.enitity;

/**
 * Created by qhuydtvt on 3/14/2016.
 */
public class AnswerChoiceVM {

    private String questionId;
    private int index;
    private String text;
    private boolean selected = false;

    public AnswerChoiceVM(String questionId, int index, String text) {
        this.questionId = questionId;
        this.text = text;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
