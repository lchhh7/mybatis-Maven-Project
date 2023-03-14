package com.jinjin.jintranetTest;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import org.junit.Test;

public class ControllerTest {

	@Test
	public void sol() {
		Stack st = new Stack();
		st.push("1");
		st.push("2");
		
		st.pop();
		System.out.println(st);
		
		Queue q = new LinkedList<>();
		q.offer("0");
		q.offer("1");
		q.offer("2");
		
		q.poll();
		System.out.println(q);
	}
}
