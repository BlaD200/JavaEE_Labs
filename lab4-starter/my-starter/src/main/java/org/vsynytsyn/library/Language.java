package org.vsynytsyn.library;

import org.springframework.beans.factory.InitializingBean;

public interface Language extends InitializingBean {

    String getLanguageCode();

    String getFullName();

    @Override
    default void afterPropertiesSet() {
        System.out.printf("Using %s language (%s).\n", getFullName(), getLanguageCode());
    }
}
