package com.ticket4u.mailservice.processor;

import com.ticket4u.mailservice.enums.TemplateType;

import java.util.Map;

public interface TemplateProcessor {

    String process(TemplateType templateType, Map<String, Object> data);

    boolean templateExists(TemplateType templateType);
}
