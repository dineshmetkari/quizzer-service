/*
 * Copyright 2017 Alexander Orlov <alexander.orlov@loxal.net>. All rights reserved.
 */

package net.loxal.quizzer.init;

import net.loxal.quizzer.PollTests;
import net.loxal.quizzer.controller.PollController;
import net.loxal.quizzer.dto.Poll;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PollTests.Config.class)
public class InitSimpsonsQuiz {
    public static final String SIMPSON_QUIZ_PREFIX = "simpsons-";
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void initSimpsonsQuiz() throws Exception {
        Poll poll1 = new Poll(
                SIMPSON_QUIZ_PREFIX + "0",
                "In which town do the Simpsons reside?",
                Arrays.asList(
                        "Springfield-",
                        "Shelbyville",
                        "Seinfeld"
                ),
                Collections.singletonList(0)
        );

        Poll poll2 = new Poll(
                SIMPSON_QUIZ_PREFIX + "1",
                "What is the name of the Simpsons' next door neighbor?",
                Arrays.asList(
                        "Barney Gumble",
                        "Ned Flanders-",
                        "Principal Skinner"
                ),
                Collections.singletonList(1)
        );

        Poll poll3 = new Poll(
                SIMPSON_QUIZ_PREFIX + "2",
                "Who founded the Simpsons’ town?",
                Arrays.asList(
                        "Jebadiah Springfield-",
                        "Zachariah Springfield",
                        "Springfield Manhattan"
                ),
                Collections.singletonList(0)
        );
        Poll poll4 = new Poll(
                SIMPSON_QUIZ_PREFIX + "3",
                "How old is Bart?",
                Arrays.asList(
                        "10-",
                        "11",
                        "12"
                ),
                Collections.singletonList(0)
        );
        Poll poll5 = new Poll(
                SIMPSON_QUIZ_PREFIX + "4",
                "What is the name of the clown on Channel 6?",
                Arrays.asList(
                        "Gabbo",
                        "Krusty-",
                        "Bonko"
                ),
                Collections.singletonList(1)
        );
        Poll poll6 = new Poll(
                SIMPSON_QUIZ_PREFIX + "5",
                "What is the name of Lisa’s jazz mentor?",
                Arrays.asList(
                        "Billy Jazzman",
                        "Blind Willy Witherspoon",
                        "Bleeding Gums Murphy-"
                ),
                Collections.singletonList(2)
        );

        Poll poll7 = new Poll(
                SIMPSON_QUIZ_PREFIX + "6",
                "Who is Mr Burns’ assistant?",
                Arrays.asList(
                        "Seymour Skinner",
                        "Barnard Gumble",
                        "Waylon Smithers-"
                ),
                Collections.singletonList(2)
        );
        Poll poll8 = new Poll(
                SIMPSON_QUIZ_PREFIX + "7",
                "What is the name of the bar where Homer drinks?",
                Arrays.asList(
                        "Moe’s Tavern-",
                        "Joe’s Cavern",
                        "The Drink Hole"
                ),
                Collections.singletonList(0)
        );
        Poll poll9 = new Poll(
                SIMPSON_QUIZ_PREFIX + "8",
                "Which one of these is not a catchphrase Bart uses?",
                Arrays.asList(
                        "Aye Carumba!",
                        "Don't have a cow, man!",
                        "Woohoo!-"
                ),
                Collections.singletonList(2)
        );
        Poll poll10 = new Poll(
                SIMPSON_QUIZ_PREFIX + "9",
                "What did the Simpsons get for their first Christmas?",
                Arrays.asList(
                        "A dog-",
                        "A cat",
                        "A hamster"
                ),
                Collections.singletonList(0)
        );
        poll10.setLastQuestion(true);

        final List<Poll> polls = Arrays.asList(
                poll1, poll2, poll3,
                poll4, poll5, poll6,
                poll7, poll8, poll9, poll10
        );

        polls.forEach(poll -> {
            System.out.println("this = " + this);
            System.out.println("this.testRestTemplate = " + this.testRestTemplate);
            System.out.println("PollController.ENDPOINT" + PollController.ENDPOINT);
            ResponseEntity<String> response = this.testRestTemplate.postForEntity(PollController.ENDPOINT, poll, String.class);
//            ResponseEntity<Poll> response = this.testRestTemplate.postForEntity(PollController.ENDPOINT, poll, Poll.class);
            System.out.println("response.getBody() = " + response.toString());
            System.out.println("response.getBody() = " + response.getBody());
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//            final Poll retrieved = response.getBody();
//            assertThat(retrieved).isInstanceOf(Poll.class);
//            assertThat(retrieved).isNotNull();
        });
    }
}


