package org.spring.training.boot.infrastructure.internal.beans;

import org.spring.training.boot.infrastructure.external.annotation.Trimmed;
import org.springframework.stereotype.Component;

@Component
@Trimmed(returnValue = true)
public class TalkingService {

    public String talk(String speech, String author, int rating) {
        System.out.println(speech);
        System.out.println(author);
        System.out.println(rating);
        return "   Talk has happened  ";
    }
}
