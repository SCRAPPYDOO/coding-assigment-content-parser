package com.content.text.configuration.txt;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TxtFileProcessor implements ItemProcessor {

    @Override
    public Object process(Object item) throws Exception {
        return item;
    }
}
