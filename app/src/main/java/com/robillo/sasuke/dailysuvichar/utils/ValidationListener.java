package com.robillo.sasuke.dailysuvichar.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.rule.NotEmptyRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasuke on 4/17/2017.
 */

public abstract class ValidationListener implements Validator.ValidationListener {

    abstract public Context getContext();

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        List<ValidationError> filteredErrors = new ArrayList<>();
        filteredErrors.addAll(errors);

        for (ValidationError error : errors) {
            View view = error.getView();
            if (view instanceof EditText) {
                if (((EditText) view).getText().toString().trim().length() == 0) {
                    boolean isRequired = false;
                    for (Rule rule : error.getFailedRules()) {
                        if (rule instanceof NotEmptyRule) {
                            isRequired = true;
                        }
                    }

                    if (!isRequired) {
                        filteredErrors.remove(error);
                    }
                }
            }
        }

        if (filteredErrors.size() == 0) {
            onValidationSucceeded();
            return;
        }

        for (ValidationError error : filteredErrors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());

            // Display finish messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
