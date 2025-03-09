package com.mikeltek.fotressmarket.forms;

import android.view.ViewGroup;
import com.mikeltek.fotressmarket.forms.components.AppTextBox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FormSet {

    private static class Field {
        int id;
        AppTextBox field;
        List<AppFormValidator> validators;
        String value = "";
        List<String> errors;
    }

    public static final AppFormValidator Required = value -> {
        if (value.isBlank())
            return java.util.Optional.of("This field is required.");
        return Optional.empty();
    };

    private final Map<Integer, Field> registry = new HashMap<>();
    private final Map<Integer, List<AppFormValidator>> validators;

    public FormSet(ViewGroup formContainer) {
        validators = new HashMap<>();
        register(formContainer);
    }

    public FormSet(ViewGroup formContainer, HashMap<Integer, List<AppFormValidator>> fieldValidators) {
        validators = fieldValidators;
        register(formContainer);
    }

    public void unregister(int id) {
        registry.remove(id);
    }

    private void register (ViewGroup formContainer) {
        int count = formContainer.getChildCount();
        for (int i = 0; i < count; i++) {
            var c = formContainer.getChildAt(i);
            if (c instanceof AppTextBox) registerField((AppTextBox) c);
        }
    }

    private void registerField(AppTextBox textBox) {
        var field = new Field();
        field.field = textBox;

        field.id = textBox.getId();
        field.validators = validators.getOrDefault(field.id, new ArrayList<>());
        registry.put(field.id, field);

        textBox.registerOnChange(s -> field.value = s);
    }

    public void register(int id, AppFormValidator validator) {
        var field = registry.get(id);
        assert field != null;
        field.validators.add(validator);
    }

    public void register(int id, List<AppFormValidator> validators) {
        var field = registry.get(id);
        assert field != null;
        field.validators.addAll(validators);
    }

    public Boolean isValid() {
        return validate();
    }
    
    private boolean validate()
    {
        var hasErrors = false;

        for (var key : registry.keySet())
        {
            Field field = registry.get(key);
            assert field != null; // This is really not needed
            field.errors = new ArrayList<>(); // reset errors

            field.validators.forEach(v -> {
                var err = v.validate(field.value);
                err.ifPresent(field.errors::add);
            });

            if (!field.errors.isEmpty()) {
                hasErrors = true;
                field.field.updateViewErrors(field.errors);
            }
            else {
                field.field.clearViewErrors();
            }
        }

        return hasErrors;
    }

    public Map<Integer, List<String>> getErrors() {
        var errors = new HashMap<Integer, List<String>>();

        for (var key : registry.keySet()) {
            Field field = registry.get(key);
            assert field != null;
            if (!field.errors.isEmpty())
                errors.put(field.id, field.errors);
        }
        return errors;
    }

    public  Map<Integer, String> getValues() {
        var values = new HashMap<Integer, String>();

        for (var key : registry.keySet()) {
            Field field = registry.get(key);
            assert field != null;
            values.put(field.id, field.value);
        }
        return values;
    }
}
