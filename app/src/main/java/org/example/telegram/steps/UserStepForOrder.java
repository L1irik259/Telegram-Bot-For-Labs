package org.example.telegram.steps;

import java.util.HashMap;
import java.util.Map;

public class UserStepForOrder {

    public enum Step {
        LANGUAGE,
        DATE,
        TECHNICALSPECIFICATION,
        TASKDESCRIPTION, // описание задачи
        DONE
    }

    private Step currentStep = Step.LANGUAGE; // начинаем с выбора языка
    private final Map<String, String> answers = new HashMap<>();

    public Step getCurrentStep() {
        return currentStep;
    }

    public void nextStep() {
        switch (currentStep) {
            case LANGUAGE -> currentStep = Step.DATE;
            case DATE -> currentStep = Step.TECHNICALSPECIFICATION;
            case TECHNICALSPECIFICATION -> currentStep = Step.TASKDESCRIPTION;
            case TASKDESCRIPTION -> currentStep = Step.DONE;
            default -> currentStep = Step.DONE;
        }
    }

    public void saveAnswer(String key, String value) {
        answers.put(key, value);
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public boolean isComplete() {
        return currentStep == Step.DONE;
    }

    
}
