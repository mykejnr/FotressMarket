package com.mikeltek.fotressmarket.forms;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FormSet {

    private static class Field {
        int id;
        List<AppFormValidator> validators;
        String value = "";
    }

    public static final AppFormValidator Required = value -> {
        if (value.isBlank())
            return java.util.Optional.of("This field is required.");
        return Optional.empty();
    };

    private final Map<Integer, Field> registry = new HashMap<>();
    private final Map<Integer, List<String>> errors = new HashMap<>();

    public void unregister(int id) {
        registry.remove(id);
    }

    public void register(EditText view) {
        ArrayList<AppFormValidator> v = new ArrayList<>();
        v.add(FormSet.Required);
        register(view, v);
    }

    public void register(EditText view, List<AppFormValidator> validators)
    {
        var field = new Field();
        field.id = view.getId();
        field.validators = validators;
        registry.put(field.id, field);

        view.addTextChangedListener(new TextWatcher() {
            private final Field f = field;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                f.value = s.toString();
            }
        });
    }

    public Boolean isValid() {
        validate();
        return errors.isEmpty();
    }
    
    public void validate()
    {
        errors.clear();

        for (var key : registry.keySet())
        {
            Field field = registry.get(key);
            assert field != null; // This is really not needed
            List<String> fieldErrors = new ArrayList<>(field.validators.size());

            field.validators.forEach(v -> {
                var err = v.validate(field.value);
                err.ifPresent(fieldErrors::add);
            });

            if (!fieldErrors.isEmpty())
                errors.put(field.id, fieldErrors);
        }
    }
}
