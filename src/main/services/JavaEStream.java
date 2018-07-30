package main.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
public class JavaEStream {

    /**
     * All the registered users.
     */
    private List<Long> userNumbers;

    public JavaEStream () {
        this.userNumbers = new ArrayList<Long>();
        for (int i=0; i<100; i++) {
            this.userNumbers.add((long) (i+1));
        }
    }

    /**
     * parallel stream
     */
    @RequestMapping("/stream")
    public ReturnMe stream(@RequestParam(value="parallel", defaultValue="parallel")String parallel) {
        ReturnMe returnMe = new ReturnMe();
        Date start = new Date();

        returnMe.threadIds = new HashSet<Long>();
        Stream<Long> streamOfNumbers;
        if (parallel.equalsIgnoreCase("parallel")) {
            streamOfNumbers = this.userNumbers.parallelStream();
        }
        else {
            streamOfNumbers = this.userNumbers.stream();
        }

        returnMe.newList = streamOfNumbers.filter(i -> {
            try {
                long currentThreadId = Thread.currentThread().getId();
                returnMe.threadIds.add(currentThreadId);
                Thread.sleep(10);
                returnMe.processingNumbers.put((Long) i, currentThreadId);
                System.out.println("number "+i+" thread "+currentThreadId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return i<50;
        }).collect(Collectors.toList());

        System.out.println(Arrays.toString(returnMe.newList.toArray()));
        System.out.println(returnMe.processingNumbers.keySet());

        Date end = new Date();
        returnMe.timeInterval = end.getTime()-start.getTime();

        return returnMe;
    }

    /**
     * Try IntStream
     */
    @RequestMapping("/intStream")
    public ReturnMe intStream(){

        System.out.println("Now running in sequencial mode.");
        boolean result = IntStream.range(2, 50).filter(i->i%2==0).filter(i->i%3==0).noneMatch(i -> {
            JavaEStream.this.printThreadsSequence();
            return i==2 || i==50;
        });

        System.out.println("Now running in parallel mode.");
        result = IntStream.range(2, 50).parallel().filter(i->i%2==0).filter(i->i%3==0).noneMatch(i -> {
            JavaEStream.this.printThreadsSequence();
            return i==2 || i==50;
        });

        return null;
    }

    private void printThreadsSequence() {
        long tId = Thread.currentThread().getId();
        System.out.println("Thread: "+tId+" is running now!");
    }

    class ReturnMe {
        public long timeInterval;
        public Set<Long> threadIds;
        public List<Long> newList;
        public Map<Long, Long> processingNumbers = new LinkedHashMap<Long, Long>();

    }
}
