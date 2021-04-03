package org.vsynytsyn.library;

import org.springframework.stereotype.Component;

@Component
public class GermanLanguage implements Language {
    @Override
    public String getLanguageCode() {
        return "de";
    }


    @Override
    public String getFullName() {
        return "Deutsche";
    }
}
