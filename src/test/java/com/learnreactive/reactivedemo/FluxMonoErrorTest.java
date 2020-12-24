package com.learnreactive.reactivedemo;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxMonoErrorTest {
    private List<String> listOfNames = Arrays.asList("A", "B", "C");
    private Flux<String> fluxOfNames = Flux.fromIterable(listOfNames);

    @Test
    public void Test_OnErrorResume() {
        Flux<String> fluxOfNamesWithError =  fluxOfNames.concatWith(Flux.error(new RuntimeException("My RuntimeException")));
        Flux<String> fluxErrorWithResume = fluxOfNamesWithError.onErrorResume((e) -> {
            System.out.println(e);
            return Flux.just("default value");
        });

        StepVerifier.create(fluxErrorWithResume.log())
                .expectNext("A","B","C","default value")
                .verifyComplete();
    }

    @Test
    public void Test_OnErrorMap() {
        Flux<String> fluxOfNamesWithError =  fluxOfNames.concatWith(Flux.error(new RuntimeException("My RuntimeException")));
        Flux<String> fluxErrorWithResume = fluxOfNamesWithError.onErrorMap((e)->{
            return new IllegalArgumentException("IllegalArgumentException");
        });

        StepVerifier.create(fluxErrorWithResume.log())
                .expectNext("A","B","C")
                .verifyError(IllegalArgumentException.class);

        StepVerifier.create(fluxErrorWithResume.log())
                .expectNext("A","B","C")
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void Test_OnErrorReturn() {
        Flux<String> fluxOfNamesWithError =  fluxOfNames.concatWith(Flux.error(new RuntimeException("My RuntimeException")));
        Flux<String> fluxErrorWithReturn = fluxOfNamesWithError.onErrorReturn("default value");

        StepVerifier.create(fluxErrorWithReturn.log())
                .expectNext("A","B","C")
                .expectNext("default value")
                .verifyComplete();
    }

    @Test
    public void Test_OnErrorContinue() {
        Flux<String> fluxOfNamesWithError =  fluxOfNames
                .map(s ->{
                    if(s == "B")
                    {
                        throw new RuntimeException("This letter is B");
                    }
                    return s;
                });

        Flux<String> fluxErrorWithReturn = fluxOfNamesWithError.onErrorContinue((e,s)->{
            System.out.println( "Error happended on the letter " + s + "; message : " + e.getMessage() );
        });

        StepVerifier.create(fluxErrorWithReturn.log())
                .expectNext("A","C")
                .verifyComplete();
    }
}
