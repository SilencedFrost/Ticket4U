package com.ticket4u.mailservice.processor;

import com.ticket4u.mailservice.enums.TemplateType;
import com.ticket4u.mailservice.exception.TemplateNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThymeleafTemplateProcessor implements TemplateProcessor {

    private final SpringTemplateEngine templateEngine;

    @Override
    public String process(TemplateType templateType, Map<String, Object> data) {
        if (!templateExists(templateType)) {
            throw new TemplateNotFoundException("Template not found: " + templateType.getTemplatePath());
        }

        Context context = new Context();
        if (data != null) {
            context.setVariables(data);
        }

        log.debug("Processing template: {}", templateType.getTemplatePath());
        return templateEngine.process(templateType.getTemplatePath(), context);
    }

    @Override
    public boolean templateExists(TemplateType templateType) {
        String path = "templates/" + templateType.getTemplatePath() + ".html";
        return new ClassPathResource(path).exists();
    }
}
