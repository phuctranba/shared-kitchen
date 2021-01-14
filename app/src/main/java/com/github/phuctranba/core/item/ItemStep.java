package com.github.phuctranba.core.item;

import java.util.List;

public class ItemStep {
    private String StepNumber;
    private String StepImage;
    private String StepDescription;
    private String StepNote;
    private List<String> StepImages;

    public List<String> getStepImages() {
        return StepImages;
    }

    public void setStepImages(List<String> stepImages) {
        StepImages = stepImages;
    }

    public String getStepNumber() {
        return StepNumber;
    }

    public void setStepNumber(String stepNumber) {
        StepNumber = stepNumber;
    }

    public String getStepImage() {
        return StepImage;
    }

    public void setStepImage(String stepImage) {
        StepImage = stepImage;
    }

    public String getStepDescription() {
        return StepDescription;
    }

    public void setStepDescription(String stepDescription) {
        StepDescription = stepDescription;
    }

    public String getStepNote() {
        return StepNote;
    }

    public void setStepNote(String stepNote) {
        StepNote = stepNote;
    }
}
