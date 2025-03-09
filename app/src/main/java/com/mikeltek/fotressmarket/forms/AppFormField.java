package com.mikeltek.fotressmarket.forms;

import java.util.List;

public interface AppFormField {
    /**
     * Called to update the view with the provided errors
     * @param errors validation errors associated with this view
     */
    void updateViewErrors(List<String> errors);

    /**
     * Get the value for this field
     * @return string representation of the value of this field
     */
    String getValue();

    /**
     * Clear the errors of this field from the view
     */
    void clearViewErrors();

    /**
     * Register a callback that is meant to be called when the
     * field is updated from the view
     * @param callBack function to call when field's value changes
     */
    void registerOnChange(AppFormFieldOnChange callBack);

    /**
     * Get the view id of this form field. This is usually automatically
     * provided if field subclasses a view
     * @return an id from the view
     */
    int getId();
}
