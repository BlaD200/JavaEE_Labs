package org.vsynytsyn.library;

import org.springframework.stereotype.Component;

@Component
public class EnglishLanguage implements Language {
    @Override
    public String getLanguageCode() {
        return "en";
    }


    @Override
    public String getFullName() {
        return "English";
    }
}
