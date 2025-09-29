package com.titoo.calculator;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Service class providing basic calculator operations.
 * <p>
 * Currently supports summing two integers.
 * </p>
 */
@Service 
public class Calculator {
     @Cacheable("sum")
     public int sum(int a, int b) {
          try {
               Thread.sleep(3000); // dummy delay to simulate a long calculation for caching demo purposes
          }
          catch (InterruptedException e) {
               e.printStackTrace();
          }
          return a + b;
     }
}
