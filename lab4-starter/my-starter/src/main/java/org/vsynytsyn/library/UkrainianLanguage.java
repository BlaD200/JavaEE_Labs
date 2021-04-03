package org.vsynytsyn.library;

import org.springframework.stereotype.Component;

@Component
public class UkrainianLanguage implements Language {
    @Override
    public String getLanguageCode() {
        return "ukr";
    }


    @Override
    public String getFullName() {
        return "Українська";
    }
}
