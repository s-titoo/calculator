package com.titoo.calculator; 
import org.springframework.stereotype.Service; 
 
/**
 * Service class providing basic calculator operations.
 * <p>
 * Currently supports summing two integers.
 * </p>
 */
@Service 
public class Calculator { 
     public int sum(int a, int b) { 
          return a + b; 
     } 
} 
