package com.mikeltek.fotressmarket.forms;

import java.util.Optional;

public interface AppFormValidator {
    abstract Optional<String> validate(String value);
}
